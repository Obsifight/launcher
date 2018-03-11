package fr.thisismac.launcher.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HashUtils {

	/**
	 * Get MD5 from a File
	 * @param file
	 * @return file's hash
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String getMD5(File file) throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		FileInputStream stream = new FileInputStream(file);
		md.reset();
		byte[] bytes = new byte[65536];
		int numBytes;
		while ((numBytes = stream.read(bytes)) != -1) {
			md.update(bytes, 0, numBytes);
		}
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < digest.length; i++) {
	        sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
	    }
		return sb.toString();
	}
	
	/**
	 * Get SHA1 Hash from String
	 * @param String
	 * @return string's hash
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String getSHA(String passwd) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("SHA1");
		byte[] hash = md.digest(passwd.getBytes("UTF-8"));
		StringBuilder sb = new StringBuilder(2 * hash.length);
		for (byte b : hash) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString();
	}
}
