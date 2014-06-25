package mariti.audio.iad;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFormat;

public class AudioUtils {
	
	/**
	 * trasforma i due bytes ordinati in big endian se <b>bigEndian</b> e' vero
	 * in un intero short
	 * @param first
	 * @param second
	 * @param bigEndian
	 * @return
	 * @throws Exception
	 */
	public static short bytes2short(byte first, byte second, boolean bigEndian)
	throws Exception {
		if (bigEndian) { byte tmp = first; first = second; second = tmp; }
		return (short)(first << 8 | second & 0xff);
	}
	
	public static int bytes2int(byte first, byte second, boolean bigEndian)
	throws Exception {
		if (bigEndian) { byte tmp = first; first = second; second = tmp; }
		return first << 8 | second & 0xff;
	}
	public static int bytes2int(byte first, byte second, byte third, 
			boolean bigEndian) throws Exception {
		if (bigEndian) { byte tmp = first; first = third; third = tmp; }
		return first << 16 | second << 8 & 0xffff | third & 0xff;
	}
	public static int bytes2int(byte first, byte second, byte third, 
			byte fourth, boolean bigEndian) throws Exception {
		if (bigEndian) { 
			byte tmp = first; first = fourth; fourth = tmp;
			tmp = second; second = third; third = tmp;
		}
		return first<<24 | (second & 0xff)<<16 | (third & 0xff)<<8 | fourth & 0xff;
	}
	
	public static byte[] int2bytes(int d, boolean bigEndian) {
		byte[] result = new byte[4];
		if (!bigEndian) {
			result[0] = (byte)(d >> 24);
			result[1] = (byte)(d >> 16);
			result[2] = (byte)(d >> 8);
			result[3] = (byte)(d);
		} else {
			result[3] = (byte)(d >> 24);
			result[2] = (byte)(d >> 16);
			result[1] = (byte)(d >> 8);
			result[0] = (byte)(d);
		}
		
		return result;
	}
	
	/**
	 * trasforma l'intero short in due bytes ordinati in big endian se 
	 * <b>bigEndian</b> e' vero
	 * @param d
	 * @param bigEndian
	 * @return
	 */
	public static byte[] short2bytes(short d, boolean bigEndian) {
		byte[] result = new byte[2];
		if (!bigEndian) {
			result[0] = (byte)(d >> 8);
			result[1] = (byte)(d);
		} else { 
			result[1] = (byte)(d >> 8);
			result[0] = (byte)(d);
		}
		return result;
	}
}
