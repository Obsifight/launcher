package fr.thisismac.launcher.elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import fr.thisismac.launcher.Core;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DownloadTask implements Runnable {

	private CustomFile file;

	@Override
	public void run() {
		FileOutputStream fos = null;

		try {
			URL url = new URL(file.getUrl());
			InputStream in = url.openConnection().getInputStream();
			File path = new File(file.getPath());
			
			fos = new FileOutputStream(path);
			byte[] buff = new byte[32768];

			int n;
			while ((n = in.read(buff)) != -1) {
				fos.write(buff, 0, n);
				Core.get().getBar().addToCurrentSize(n);
				Core.get().getBar().repaint();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				System.out.println("downloaded : " + file.getUrl());
				Core.get().getBar().decrementeFileLeft();
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}