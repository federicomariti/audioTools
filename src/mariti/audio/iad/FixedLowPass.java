package mariti.audio.iad;

public class FixedLowPass extends AbstractModule {
    private FIR fir = new FIR();
    //default is for 44100 sample rate
    private Float[] coef = { 0.000360F, 0.000784F, 0.001401F, 0.002241F, 0.003330F, 0.004680F, 0.006291F, 0.008148F, 0.010218F, 0.012452F, 0.014786F, 0.017144F, 0.019438F, 0.021579F, 0.023478F, 0.025053F, 0.026233F, 0.026963F, 0.027211F, 0.026963F, 0.026233F, 0.025053F, 0.023478F, 0.021579F, 0.019438F, 0.017144F, 0.014786F, 0.012452F, 0.010218F, 0.008148F, 0.006291F, 0.004680F, 0.003330F, 0.002241F, 0.001401F, 0.000784F, 0.000360F};

    public FixedLowPass() {
	fir.setCoefficients(coef);
    }

    public void filter(byte[] data, int off, int len) {
	if (null == af) return;
	fir.setAudioFormat(af);
	fir.filter(data, off, len);
    }
}
