package mariti.audio.iad.gui;

import mariti.audio.iad.PeakNormalization;

import javax.sound.sampled.AudioFormat;

import java.text.DecimalFormat;

import javax.swing.JPanel;
import javax.swing.JLabel;
//import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

//import java.awt.event.ActionListener;
//import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class PeakNormalizationGUI extends JPanel implements ChangeListener, TabElement {
    private JLabel peakLabel;
    private JLabel dbLabel;
    //private JComboBox combo;
    private JSpinner peakSpinner;
    private PeakNormalization normalizer;

    public PeakNormalizationGUI () {
	normalizer = new PeakNormalization(100);

	//Integer[] values = { 100, 98 };
	//combo = new JComboBox(values);
	SpinnerNumberModel spinnerModel =
	    new SpinnerNumberModel(100, 50, 100, 1);

	peakSpinner = new JSpinner(spinnerModel);
	peakLabel = new JLabel("peak value");
	dbLabel = new JLabel("0db");//new DecimalFormat("#.##").format(0) + "db");

	//combo.addActionListener(this);
	peakSpinner.addChangeListener(this);

	//add(combo);
	add(peakLabel);
	add(peakSpinner);
	add(dbLabel);
    }

    // public void actionPerformed(ActionEvent e) {
    // 	Integer i = (Integer)combo.getSelectedItem();
    // 	normalizer.setPeak(i);
    // }
    public void stateChanged(ChangeEvent e) {
	int i = (Integer)peakSpinner.getValue();
	normalizer.setPeak(i);
	double dbvalue = 20 * Math.log10(i/100F);
	dbvalue = Math.floor(dbvalue * 100) / 100;
	//dbLabel.setText(new DecimalFormat("#.##").format(dbvalue) + "db");
	dbLabel.setText(dbvalue + "db");
    }

    public void filter(byte[] data, int off, int len) {
	normalizer.filter(data, off, len);
    }
    public AudioFormat getAudioFormat() {
	return normalizer.getAudioFormat();
    }
    public void setAudioFormat(AudioFormat af) {
	normalizer.setAudioFormat(af);
    }
}
