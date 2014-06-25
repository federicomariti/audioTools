package mariti.audio.iad;

import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFormat;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import java.io.PrintWriter;

@Retention(RetentionPolicy.RUNTIME)
    @interface MethodSpecification {
	String shortOpt();
	String longOpt();
	boolean hasArg() default false;
	String description() default "";
	String argName() default "";
	boolean isRequired() default false;
	char valueSeparator() default '=';
    }
		     
public class CommandLineInterface {

    private static byte[] map;
    private static Module module;
    private static AudioFormat af;

    @MethodSpecification( shortOpt = "f",
			  longOpt = "file",
			  hasArg = true,
			  description = "The input audio file",
			  argName = "fileName",
			  isRequired = true)

    private static void inputFile(String value, CommandLine cl)
	throws UnsupportedAudioFileException, IOException{
	AudioInputStream ais =
	    AudioSystem.getAudioInputStream(new File(value));
	af = ais.getFormat();
	System.out.println("file length in bytes" + ais.available());
	map = new byte[ais.available()];
	System.out.println("file content allocated in memory");
    }

    @MethodSpecification( shortOpt = "o",
			  longOpt = "output-file",
			  hasArg = true,
			  description = "The output audio file",
			  argName = "fileName",
			  isRequired = true)

    private static void outputFile(String value, CommandLine cl) {
	//TODO
    }

    @MethodSpecification( shortOpt = "m",
			  longOpt = "module",
			  hasArg = true,
			  description = "The audio processig module name",
			  argName = "moduleName",
			  isRequired = true)

    private static void module(String value, CommandLine cl)
	throws Exception {
	try {
	    Method m = CommandLineInterface.class.
		getDeclaredMethod(value,CommandLine.class);
	    m.invoke(null, cl);
	    module.filter(map, 0, map.length);
	} catch(Exception e) { ; }
    }

    @MethodSpecification( shortOpt = "a",
			  longOpt = "argument",
			  hasArg = true,
			  description = "")

    private static void args(String value, CommandLine cl) {
	//DO NOTHING
    }

    private static void volume(CommandLine cl) throws Exception {
	String[] args = cl.getOptionValues('a');
	float volume = -1;
	String argName = "volume=";
	for (int i=0; i<args.length; i++)
	    if (args[i].startsWith(argName)) {
		volume = Integer.
		    valueOf(args[i].substring(argName.length())) / 100F;
		break;
	    }
	if (volume < 0) 
	    throw new Exception("Error: no volume value specified");
	module = new Volume(af, volume);
    }

    private static void peakNormalizer(CommandLine cl) throws Exception {
	String[] args = cl.getOptionValues('a');
	int threshold = 0;
	String argName = "peak=";
	for (int i=0; i<args.length; i++)
	    if (args[i].startsWith(argName)) {
		threshold = Integer.valueOf(args[i].substring(argName.length()));
		break;
	    }
	if (threshold == 0) threshold = 100;
	if (threshold < 98 || threshold >100)
	    throw new Exception("Error: ");
	module = new PeakNormalization(af, threshold);
    }

    private static void lowPass(CommandLine cl) throws Exception {
	String[] args = cl.getOptionValues('a');
	float[] coef = null;
	String fileName = null;
	String[] argName = { "coef-file=", "coef=" };
	for (int i=0; i<args.length; i++)
	    if (args[i].startsWith(argName[0])) {
		fileName = args[i];
		break;
	    } else if (args[i].startsWith(argName[1])) {
		break;
	    }
	if (null == fileName || null == coef)
	    throw new Exception("Error: no coefficients specified");
	module = new FIR(af);
	//module.setCoefficients();
    }


    @MethodSpecification( shortOpt = "h",
			  longOpt = "help",
			  description = "Shows the help content"
			  )
    private static void printHelp(HelpFormatter hf, Options options) {
	PrintWriter out = new PrintWriter(System.out);
	int width = 80;
	hf.printUsage(out, width, "-f inputFile -m moduleName " +
		      "[-a argName=value ...] " +
		      "-o outputFile");
	hf.printOptions(out, width, options, 2, 1);
	hf.printWrapped(out, width, 2,
			"Modules arguments: \n" +
			"volume: -a volume=<integer> \n" +
			"peakNormalization: nothing\n" +
			"lowPass: -a file=<fileName> \n" +
			"lowPass: -a coefficients=<float,float,...>");
	out.close();
    }

    public static void main(String[] args) throws Exception {
	HashMap<String, Method> optMap = new HashMap<String, Method>();
	Options options = new Options();
	Options helpOptions = new Options();
	Method[] methods = CommandLineInterface.class.getDeclaredMethods();
	for (int i=0; i<methods.length; i++) {
	    //method's annotation
	    MethodSpecification a =
		methods[i].getAnnotation(MethodSpecification.class);
	    if (null != a) {
		Option opt = new Option(a.shortOpt(), a.longOpt(), a.hasArg(),
					a.description());
		opt.setArgName(a.argName());
		opt.setRequired(a.isRequired());
		opt.setValueSeparator(a.valueSeparator());
		options.addOption(opt);
		if ("h" == a.shortOpt())
		    helpOptions.addOption(opt);
		optMap.put(a.shortOpt(), methods[i]);
	    }

	}

	HelpFormatter hf = new HelpFormatter();
	CommandLineParser clp = new PosixParser();
	CommandLine cl = null;
	String[] parametriNecessari = { "file", "output-file", "module" };
		
	try {
	    cl = clp.parse(options, args);
	} catch (Exception e) { 
	    try {
		cl = clp.parse(helpOptions, args);
		if (cl.hasOption('h')) {
		    printHelp(hf, options);
		} else {
		    System.err.println(e.getMessage());
		    printHelp(hf, options);
		    System.exit(1);
		}
	    } catch(Exception e2) {
		// do nothing
	    }
	    if (null == cl) {
		System.err.println(e.getMessage());
		printHelp(hf, options);
		System.exit(1);
	    } else
		System.exit(0);
	}

	if (cl.hasOption('h')) {
	    printHelp(hf, options);
	    System.exit(0);
	}

	Option[] receivedOpts = cl.getOptions();
	try {
	    for (int i = 0; i < receivedOpts.length; i++) {
		Method handler = optMap.get(receivedOpts[i].getOpt());
		if (null != handler)
		    handler.invoke(null, receivedOpts[i].getValue(), cl);
	    }
	} catch(Exception e) {
	    throw new Exception(e);
	}
    }

    // public static void main_(String[] args) throws Exception {

    // 	HashMap<String,Method> optTable = new HashMap<String,Method>();
    // 	Method[] methods = CommandLineInterface.class.getDeclaredMethods();
    // 	for (int i=0; i<methods.length; i++) {
    // 	    String next = methods[i].getName();
    // 	    if (next.startsWith("optHandle_")) 
    // 		optTable.put(next.substring("optHandle_".length()), methods[i]);
    // 	}
	

    // 	Options options = new Options();
    // 	Options helpOptions = new Options();
    // 	{
    // 	    //con una certa convenzione sui nomi dei gestori dei
    // 	    //comandi si potrebbe inizializzare option in modo
    // 	    //automatico, ad esempio la seguente convenzione:
    // 	    //  optHandle_shortOpt_longOpt_argName_isRequired(CommandLine cl)
    // 	    //ad esempio:
    // 	    //  optHandle_f_file_fileName_req(CommandLine cl)
    // 	    //  optHandle_h_help(CommandLine cl)

    // 	    Option o = new Option("h", "help", false, "Show the help content");
    // 	    helpOptions.addOption(o);
    // 	    options.addOption(o);

    // 	    o = new Option("f", "file", true, "The input audio file");
    // 	    o.setArgName("fileName");
    // 	    o.setRequired(true);
    // 	    o.setType(String.class);
    // 	    options.addOption(o);

    // 	    o = new Option("o", "outputFile", true, "The output audio file");
    // 	    o.setArgName("fileName");
    // 	    o.setRequired(true);
    // 	    options.addOption(o);

    // 	    o = new Option("m", "module", true, "The processig audio module name");
    // 	    o.setArgName("moduleName");
    // 	    o.setRequired(true);
    // 	    options.addOption(o);

    // 	    /*
    // 	    o = new Option("v", "volume", false, "The volume module");
    // 	    options.addOption(o);

    // 	    o = new Option("n", "peakNormalizer", false, "The peakNormalizer module");
    // 	    options.addOption(o);

    // 	    o = new Option("l", "lowPass", false, "The lowPass module");
    // 	    options.addOption(o);
    // 	    */
    // 	}

    // 	HelpFormatter hf = new HelpFormatter();
    // 	CommandLineParser clp = new PosixParser();
    // 	CommandLine cl = null;
    // 	String[] parametriNecessari = { "file", "output-file", "module" };
		
    // 	try {
    // 	    cl = clp.parse(options, args);
    // 	} catch (Exception e) { 
    // 	    try {
    // 		cl = clp.parse(helpOptions, args);
    // 		if (cl.hasOption('h')) {
    // 		    printHelp(hf, options);
    // 		} else {
    // 		    System.err.println(e.getMessage());
    // 		    printHelp(hf, options);
    // 		    System.exit(1);
    // 		}
    // 	    } catch(Exception e2) {
    // 		// do nothing
    // 	    }
    // 	    if (null == cl) {
    // 		System.err.println(e.getMessage());
    // 		printHelp(hf, options);
    // 		System.exit(1);
    // 	    } else
    // 		System.exit(0);
    // 	}

    // 	if (cl.hasOption('h')) {
    // 	    printHelp(hf, options);
    // 	    System.exit(0);
    // 	}

    // 	//System.out.println(Arrays.toString(cl.getOptions()));
    // 	//System.out.println(cl.getOptions()[0].getLongOpt());

    // 	Option[] receivedOpts = cl.getOptions();
    // 	try {
    // 	    for (int i = 0; i < receivedOpts.length; i++) {
    // 		String next = receivedOpts[i].getLongOpt();
    // 		Method handler = null;
    // 		if (null != next)
    // 		    handler = optTable.get(next);
    // 		if (null != handler)
    // 		    handler.invoke(null, cl);
    // 		System.out.println(handler.toGenericString());
    // 		Annotation[] annotations = handler.getAnnotations();
    // 		System.out.println(Arrays.toString(annotations));
    // 	    }
    // 	} catch(Exception e) {
    // 	    throw new Exception(e);
    // 	}

	

    // 	/*

    // 	  	ais.read(map, 0, ais.available());
    // 		System.out.println("read");
    // 	*/
    // }
}