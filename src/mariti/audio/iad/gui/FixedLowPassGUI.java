package mariti.audio.iad.gui;

import mariti.audio.iad.FixedLowPass;
import javax.swing.JPanel;
import javax.sound.sampled.AudioFormat;

public class FixedLowPassGUI extends JPanel implements TabElement {
    FixedLowPass lowPass = new FixedLowPass();

    public void filter(byte[] data, int off, int len) {
	lowPass.filter(data, off, len);
    }
    public AudioFormat getAudioFormat() {
	return lowPass.getAudioFormat();
    }
    public void setAudioFormat(AudioFormat af) {
	lowPass.setAudioFormat(af);
    }
}