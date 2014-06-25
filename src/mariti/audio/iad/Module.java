package mariti.audio.iad;

import javax.sound.sampled.AudioFormat;

public interface Module {
    void filter(byte[] data, int off, int len);
    AudioFormat getAudioFormat();
    void setAudioFormat(AudioFormat af);
}
