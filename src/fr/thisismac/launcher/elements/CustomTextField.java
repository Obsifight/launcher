package fr.thisismac.launcher.elements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import fr.thisismac.launcher.Core;
import fr.thisismac.launcher.utils.ConfigUtils;

public class CustomTextField extends JTextField {
	
	private Image background;
	private int x, y; 
	
	public CustomTextField(int x, int y, String path) {
		try {
			background = ImageIO.read(CustomTextField.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BufferedImage tmp = (BufferedImage) background;
		setBounds(x, y, tmp.getWidth(), tmp.getHeight());
		
		setFocusTraversalKeysEnabled(true);
		setOpaque(false);
		setBorder(new EmptyBorder(0, 8, 4, 0));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(background, x, y, getWidth(), getHeight(), this);
        super.paintComponent(g);
	}
}
