package mariti.audio.iad.gui;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class OutputFileGui extends SelectFileGui {

    public OutputFileGui(String name) {
	super(name, System.getProperties().getProperty("user.dir") + "/out.wav");
	setDialogType(SAVE_TYPE);
    }

    public void setFile(String fileName) {
	text.setText(fileName);
    }

    public void actionPerformed(ActionEvent e) {
	super.actionPerformed(e);
    }

}