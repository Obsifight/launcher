package fr.thisismac.launcher.elements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import fr.thisismac.launcher.Core;
import fr.thisismac.launcher.utils.ConfigUtils;

public class CustomBackground extends JPanel {

	public Image bg;
	
	public CustomBackground() {
		try {
			bg = ImageIO.read(Core.class.getResourceAsStream("/background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(bg, 0, 0, ConfigUtils.SIZE_X, ConfigUtils.SIZE_Y, this);
	}	
}
