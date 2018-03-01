package security;


import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Abdelkader Laouid
 * @author Massinissa Saoudi
 */

public class Blowfish 
{
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public String key;
	public String message;
	
	public Blowfish(String key, String message)
	{
		this.key = key;
		this.message = message;
	}
	
// Converts byte array to hex string
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
// Converts hex string to byte array  
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}

	public String encrypt() throws Exception{
		byte[] byteKey	= key.getBytes();
		String IV  	= "12345678";
		
// Create new Blowfish cipher
		SecretKeySpec keySpec = new SecretKeySpec(byteKey, "Blowfish"); 
		Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding"); 
	
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, new javax.crypto.spec.IvParameterSpec(IV.getBytes())); 
		byte [] encoding = cipher.doFinal(message.getBytes());
		return bytesToHex(encoding);
	}
	public String decrypt() throws Exception{
		byte[] byteKey	= key.getBytes();
		String IV  	= "12345678";
		
// Create new Blowfish cipher
		SecretKeySpec keySpec = new SecretKeySpec(byteKey, "Blowfish"); 
		Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding"); 
 
		cipher.init(Cipher.DECRYPT_MODE, keySpec, new javax.crypto.spec.IvParameterSpec(IV.getBytes()));
		byte [] coded = hexStringToByteArray(message);
		byte[] decoded = cipher.doFinal(coded);
		
		return new String(decoded);
	}
}