package mariti.audio.iad;

import javax.sound.sampled.AudioFormat;

public abstract class AbstractModule implements Module {
    protected AudioFormat af;

    protected AbstractModule() {
	af = null;
    }
    protected AbstractModule(AudioFormat af) {
	this.af = af;
    }

    public AudioFormat getAudioFormat() {
	return af;
    }
    public void setAudioFormat(AudioFormat af) {
	this.af = af;
    }
}