package fr.thisismac.launcher.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class EncryptUtils {

	public final static File lastlogin = new File(DirUtils.getWorkingDirectory() + File.separator + "lastlogin");
	
	public static void readLastLogin(JTextField username, JPasswordField passwd) {
		if (!lastlogin.exists()) return ;
		
		try {
			final Cipher ciph = openCipher(2);
			final DataInputStream dis = new DataInputStream(new CipherInputStream(new FileInputStream(lastlogin), ciph));

			username.setText(dis.readUTF());
			passwd.setText(dis.readUTF());
			dis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveLastLogin(String username, String password) {
		if (!lastlogin.exists()) {
			try {
				lastlogin.createNewFile();
			} catch (IOException e) {}
		}
		
		try {
			File lastLogin = new File(DirUtils.getWorkingDirectory() + File.separator + "lastlogin");
			final Cipher ciph = openCipher(1);
			final DataOutputStream dos = new DataOutputStream(new CipherOutputStream(new FileOutputStream(lastLogin), ciph));

			dos.writeUTF(username);
			dos.writeUTF(password);
			dos.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private static Cipher openCipher(final int mode) throws Exception {
		final Random rnd = new Random(43287234L);
		final byte[] data = new byte[8];
		rnd.nextBytes(data);
		final PBEParameterSpec spec = new PBEParameterSpec(data, 5);
		final SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec(ConfigUtils.NAME.toCharArray()));
		final Cipher ret = Cipher.getInstance("PBEWithMD5AndDES");
		ret.init(mode, key, spec);
		return ret;
	}
}
