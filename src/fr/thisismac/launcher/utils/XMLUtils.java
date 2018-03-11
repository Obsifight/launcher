package fr.thisismac.launcher.utils;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import fr.thisismac.launcher.Core;
import fr.thisismac.launcher.elements.CustomFile;

public class XMLUtils {

	public static void prepareJars(List<CustomFile> toDownload) {
		try {
			InputStream xml = new URL(ConfigUtils.REPO_URL + "bin.xml").openStream();
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml);
			Node node = doc.getFirstChild();
			for (int i = 0; i < node.getChildNodes().getLength(); i++) {
				Node parse = node.getChildNodes().item(i);
				if (parse.getNodeName().equals("Contents")) {
					
					String key = parse.getChildNodes().item(1).getTextContent().replace("\\", "/");
					String md5 = parse.getChildNodes().item(3).getTextContent().replace("\"", "");
					int size = Integer.parseInt(parse.getChildNodes().item(5).getTextContent());
					
					if(size > 0) {
						File local = new File(DirUtils.getWorkingDirectory(), key);
						
						if (!local.exists() || !HashUtils.getMD5(local).equals(md5)) {
							System.out.println(local.getAbsolutePath() + " will be downloaded");
							toDownload.add(new CustomFile(size, ConfigUtils.REPO_URL + key, local.getAbsolutePath()));
						}
					}
					else {
						File local = new File(DirUtils.getWorkingDirectory(), key);
						if(!local.exists())
							local.mkdirs();
					}
				}
			}
			xml.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static void prepareRessources(List<CustomFile> toDownload) {
		try {
			InputStream xml = new URL(ConfigUtils.REPO_URL + "assets.xml").openStream();
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml);
			Node node = doc.getFirstChild();
			for (int i = 0; i < node.getChildNodes().getLength(); i++) {
				Node parse = node.getChildNodes().item(i);
				
				if (parse.getNodeName().equals("Contents")) {
					String key = parse.getChildNodes().item(1).getTextContent().replace("\\", "/");
					String md5 = parse.getChildNodes().item(3).getTextContent().replace("\"", "");
					int size = Integer.parseInt(parse.getChildNodes().item(5).getTextContent());
					
					if(size > 0) {
						File local = new File(DirUtils.getWorkingDirectory(), key);
						
						if (!local.exists() || !HashUtils.getMD5(local).equals(md5)) {
							System.out.println(local.getAbsolutePath() + " will be downloaded");
							toDownload.add(new CustomFile(size, ConfigUtils.REPO_URL + key, local.getAbsolutePath()));
						}
					}
					else {
						File local = new File(DirUtils.getWorkingDirectory(), key);
						if(!local.exists())
							local.mkdirs();
					}
				}

			}
			xml.close();

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}
