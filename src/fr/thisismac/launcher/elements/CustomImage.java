package fr.thisismac.launcher.elements;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class CustomImage extends JPanel {
	
	private Image background;
	
	public CustomImage(int x, int y, String path) {
		try {
			background = ImageIO.read(CustomTextField.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedImage tmp = (BufferedImage) background;
		setBounds(x, y, tmp.getWidth(), tmp.getHeight());
		
		setOpaque(false);
		setBorder(null);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        super.paintComponent(g);
	}
}
