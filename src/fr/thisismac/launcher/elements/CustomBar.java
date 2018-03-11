package fr.thisismac.launcher.elements;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JProgressBar;

import fr.thisismac.launcher.utils.ConfigUtils;
import lombok.Getter;
import lombok.Setter;

public class CustomBar extends JProgressBar {

	private Image image;
	private int x, y;
	
	@Getter @Setter boolean download;
	@Setter String displayMessage;
	@Setter @Getter int maximum_size;
	@Getter @Setter public int current_size;
	@Setter @Getter int file_left;

	public CustomBar(int x, int y, String texture_path) {

		try {
			image = ImageIO.read(CustomBar.class.getResource(texture_path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedImage tmp = (BufferedImage) image;
		
		setBounds(x, y, tmp.getWidth(), tmp.getHeight());
		setBorder(null);
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.white);
		
		int percent;
		if (maximum_size > 0)
			percent = (int) (((float)current_size / maximum_size) * 100);
		else
			percent = 0;
		
		g.drawImage(image, x , y, (int) ((double) getWidth() * ((double) percent / 100.0)), getHeight(), this);

		FontMetrics fm = g.getFontMetrics();
		if (fm == null) return;
		
		if (download) {
			String str = percent + " % / " + file_left + " fichiers restants";
			g.drawString(str, x + (getWidth() / 2) - (fm.stringWidth(str) / 2), y + getHeight() / 2 + 3);
		} 
		else if (displayMessage != null && displayMessage.length() > 0) 
			g.drawString(displayMessage, x + (getWidth() / 2) - (fm.stringWidth(displayMessage) / 2), y + getHeight() / 2 + 3);
	
	}
	
	public void addToCurrentSize(int i) {
		current_size += i;
	}
	
	public void decrementeFileLeft() {
		file_left--;
	}
}