package mariti.audio.iad.gui;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.sound.sampled.AudioFormat;

public class InputFileGui extends SelectFileGui {
    AudioFormat af;
    
    public InputFileGui(String file) {
	super(file);
    }

    public InputFileGui(String file, AudioFormat af) {
	super(file);
	this.af = af;
    }

    public void actionPerformed(ActionEvent e) {
	super.actionPerformed(e);
    }
}