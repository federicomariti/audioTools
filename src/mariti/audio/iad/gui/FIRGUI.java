package mariti.audio.iad.gui;

import mariti.audio.iad.FIR;

import javax.sound.sampled.AudioFormat;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Insets;

import java.util.Scanner;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Locale;
import java.io.File;

public class FIRGUI extends JPanel implements ActionListener,
					      FocusListener, TabElement {
    private JButton parseButton;
    private SelectFileGui selectFile;
    private FIR fir;
    private JTextField regexpText;
    private JLabel regexpLabel;
    private JTextArea coefText;

    public FIRGUI() {
	fir = new FIR();

	BoxLayout boxly = new BoxLayout(this, BoxLayout.Y_AXIS);
	setLayout(boxly);

	selectFile = new SelectFileGui("Coefficients file");

	JPanel regexpPanel = new JPanel();
	regexpPanel.setLayout(new BoxLayout(regexpPanel, BoxLayout.X_AXIS));
	regexpText = new JTextField("\\p{Punct}*\\p{Space}+");
	regexpLabel = new JLabel("Delimiter Regular Expression");
	regexpPanel.add(regexpLabel);
	regexpPanel.add(regexpText);

	JPanel coefPanel = new JPanel();
	coefPanel.setLayout(new BorderLayout());
	parseButton = new JButton("Parse file");
	coefText = new JTextArea(5, 20);
        coefText.setMargin(new Insets(5,5,5,5));
	coefText.setLineWrap(true);
	JScrollPane coefScrollPane = new JScrollPane(coefText);
	coefPanel.add(parseButton, BorderLayout.EAST);
	coefPanel.add(coefScrollPane, BorderLayout.CENTER);

	parseButton.addActionListener(this);
	coefText.addFocusListener(this);

	add(selectFile);
	add(regexpPanel);
	add(coefPanel);
    }

    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == parseButton) {
	    //TODO: parse file and shows coefficients in text fileds
	    File file = selectFile.getSelectedFile();
	    try {
		coefText.setText("");
		Scanner scanner = new Scanner(file);
		scanner.useLocale(new Locale("US"));
		scanner.useDelimiter(regexpText.getText());
		ArrayDeque<Float> stack = new ArrayDeque<Float>();
		while(scanner.hasNextFloat())
		    stack.push(scanner.nextFloat());
		int size = stack.size();
		if (size > 0) {
		    Float[] array = stack.toArray(new Float[size]);
		    String string = Arrays.toString(array);
		    fir.setCoefficients(array);
		    coefText.setText(string.substring(1, string.length()-1));
		}
		scanner.close();
	    } catch(Exception ex) {
		System.err.println(ex.getMessage());
	    }
	}
    }

    public void focusGained(FocusEvent e) {
    }
    public void focusLost(FocusEvent e) {
	if (e.getSource() == coefText) {
	    Scanner scanner = new Scanner(coefText.getText());
	    scanner.useLocale(new Locale("US"));
	    scanner.useDelimiter("\\p{Punct}\\p{Space}");
	    ArrayDeque<Float> stack = new ArrayDeque<Float>();
	    while(scanner.hasNextFloat())
		stack.push(scanner.nextFloat());
	    int size = stack.size();
	    if (size > 0) {
		Float[] array = stack.toArray(new Float[size]);
		fir.setCoefficients(array);
	    }
	}
    }
    public void filter(byte[] data, int off, int len) {
	fir.filter(data, off, len);
    }
    public AudioFormat getAudioFormat() {
	return fir.getAudioFormat();
    }
    public void setAudioFormat(AudioFormat af) {
	fir.setAudioFormat(af);
    }
}
