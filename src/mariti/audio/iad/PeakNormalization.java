package mariti.audio.iad;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;

public class PeakNormalization extends AbstractModule {
    int peak;

    public PeakNormalization() {
	this(null, 100);
    }
    public PeakNormalization(int peak) {
	this(null, peak);
    }
    public PeakNormalization(AudioFormat af) {
	this(af, 100);
    }
    public PeakNormalization(AudioFormat af, int peak) {
	super(af);
	this.peak = peak;
    }

    public void filter(byte[] data, int off, int len) {
	if (null == af) return ;
	int sampleSize = af.getSampleSizeInBits()/8;
	boolean isBigEndian = af.isBigEndian();
	int upperBound = 1 << af.getSampleSizeInBits();
	if (af.getEncoding() == AudioFormat.Encoding.PCM_SIGNED) {
	    upperBound = upperBound / 2;
	}
	upperBound--;
	int max = 0;
	float gain;
	try {
	    for (int i=off; i<len; i+=sampleSize) {
		int j = AudioUtils.bytes2int(data[i], data[i+1], !isBigEndian);
		if (j > max) 
		    max = j; 
		else if (-j > max)
		    max = -j; 
	    }
	    gain = upperBound * (peak/100F) / max;

	    if (gain > 0) {
		for (int i=off; i<len; i+=sampleSize) {
		    int j = AudioUtils.bytes2int(data[i], data[i+1], !isBigEndian);
		    j *= gain;
		    byte[] tmp = AudioUtils.int2bytes((int)j, !isBigEndian);
		    data[i] = tmp[0];
		    data[i+1] = tmp[1];
		}
		System.out.println(max);
	    }
	} catch(Exception e) { e.printStackTrace(); }

    }

    public int getPeak() {
	return peak;
    }
    public void setPeak(int peak) {
	this.peak = peak;
    }
}