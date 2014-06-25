package mariti.audio.iad.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.BoxLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

import java.io.File;

public class SelectFileGui extends JPanel implements ActionListener, FocusListener {
    public static final int OPEN_TYPE = 0;
    public static final int SAVE_TYPE = 1;

    protected JLabel label;
    protected JButton searchButton;
    protected JTextField text;
    protected JFileChooser fileChooser;
    protected File file;
    protected int type;

    public SelectFileGui(String labelText) {
	this(labelText, System.getProperties().getProperty("user.dir"));
    }
    public SelectFileGui(String labelText, String path) {
	this(labelText, path, OPEN_TYPE);
    }
    public SelectFileGui(String labelText, String path, int dialogType) {
	file = new File(path);
	BoxLayout boxly = new BoxLayout(this, BoxLayout.LINE_AXIS);
	this.setLayout(boxly);
	label = new JLabel(labelText);
	text = new JTextField(path);
	text.setColumns(20);
	text.addFocusListener(this);
	fileChooser = new JFileChooser(System.getProperties().getProperty("user.dir"));
	if (dialogType == OPEN_TYPE)
	    searchButton = new JButton("Search File");
	else if (dialogType == SAVE_TYPE)
	    searchButton = new JButton("Open File");
	else
	    searchButton = new JButton("Search File");
	searchButton.addActionListener(this);
	add(label);
	add(text);
	add(searchButton);
    }

    public void setDialogType(int type) {
	this.type = type;
    }
    public File getSelectedFile() {
	return file;
    }
    public void setFile(File file) {
	this.file = file;
	text.setText(file.getAbsolutePath());
    }


    public void actionPerformed(ActionEvent e) {
	int returnvalue;
	switch(type) {
	default:
	case OPEN_TYPE:
	    returnvalue = fileChooser.showOpenDialog(SelectFileGui.this);
	    break;
	case SAVE_TYPE:
	    returnvalue = fileChooser.showSaveDialog(SelectFileGui.this);
	    break;
	}
	if (returnvalue == JFileChooser.APPROVE_OPTION) {
	    file = fileChooser.getSelectedFile();
	    text.setText(file.getAbsolutePath());
	}
    }
    public void focusGained(FocusEvent e) {
    }
    public void focusLost(FocusEvent e) {
	if (e.getSource() == text) {
	    file = new File(text.getText());
	}
   }
}