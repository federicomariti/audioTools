package mariti.audio.iad;

import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;

public class Test {
    public static void main(String[] args) throws Exception {

	AudioInputStream ais = AudioSystem.getAudioInputStream(new File("500 miles high.wav"));
	System.out.println(ais.available());
	byte[] map = new byte[ais.available()];
	System.out.println("allocated");
	ais.read(map, 0, ais.available());
	System.out.println("read");
    }
}