package mariti.audio.iad;

import javax.sound.sampled.AudioFormat;
import java.util.Arrays;
import java.io.File;

public class FIR extends AbstractModule {
    private Float[] coef;

    public FIR() {
	super(null);
    }
    public FIR(AudioFormat af) {
	super(af);
    }

    public void filter(byte[] data, int off, int len) {
	if (null == af) return ;
	int sampleSize = af.getSampleSizeInBits()/8;
	boolean isBigEndian = af.isBigEndian();
	//define: tail is the index of the next buffer element to
	//        write, that is, tail is the index of the oldest
	//        element in buffer
	int tail = 0;
	int[] buffer = new int[coef.length];
	int result;

	try {
	    for (int i=off; i<len; i+=sampleSize) {
		//y[i] = 0
		result = 0;
		//x[i]
		buffer[tail] =
		    AudioUtils.bytes2int(data[i], data[i+1], !isBigEndian);
		for (int j=0; j<coef.length; j++) {
		    if (i-j > 0 && i-j < len) {
			//y[i] = y[i] + c[j] * x[i-j]
			int index;
			if (tail-j < 0) {
			    index = buffer.length + tail - j;
			} else
			    index = tail-j;
			result += coef[j] * buffer[index];
		    }
		}
		byte[] tmp = AudioUtils.int2bytes((int)result, !isBigEndian);
		data[i] = tmp[0];
		data[i+1] = tmp[1];
		//tail = (tail+1) % coef.length;
		if (tail == coef.length - 1)
		    tail = 0;
		else
		    tail ++;
	    }
	} catch(Exception e) { e.printStackTrace(); }
    }

    public Float[] getCoefficients() {
	return coef;
    }
    public void setCoefficients(Float[] coefficients) {
	coef = coefficients;
    }
}