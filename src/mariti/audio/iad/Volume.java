package mariti.audio.iad;

import javax.sound.sampled.AudioFormat;

public class Volume extends AbstractModule {
    float volume;

    public Volume() {
	this(null, 1F);
    }
    public Volume(AudioFormat af) {
	this(af, 1F);
    }
    public Volume(AudioFormat af, float volume) {
	super(af);
	this.volume = volume;
    }

    public void filter(byte[] data, int off, int len) {
	if (null == af) { System.err.printf("[Volume] AudioFormat is null"); return ; }
	int sampleSize = af.getSampleSizeInBits()/8;
	boolean isBigEndian = af.isBigEndian();
	try {
	    for (int i=off; i<len; i+=sampleSize) {
		int j = AudioUtils.bytes2int(data[i], data[i+1], !isBigEndian);
		j = (int)(j * (float)volume);
		byte[] tmp = AudioUtils.int2bytes((int)j, !isBigEndian);
		data[i] = tmp[0];
		data[i+1] = tmp[1];
	    }
	} catch(Exception e) { e.printStackTrace(); }
    }
    
    public float getVolume() {
	return volume;
    }
    public void setVolume(float volume) {
	this.volume = volume;
    }
}