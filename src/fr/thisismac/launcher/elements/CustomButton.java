package fr.thisismac.launcher.elements;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class CustomButton extends JButton implements MouseListener {

	private Image normal, hover;
	public boolean onHover = false;

	/**
	 * A button with a custom texture
	 * 
	 * @param Integer the X position where the button start
	 * @param Integer the Y position where the button start
	 * @param String path for the normal texture
	 * @param String path for the hover texture
	 */
	public CustomButton(int x, int y, String normal_path, String hover_path) {
		
		try {
			normal = ImageIO.read(CustomButton.class.getResourceAsStream(normal_path));
			if (hover_path == null)
				hover = normal;
			else
				hover = ImageIO.read(CustomButton.class.getResourceAsStream(hover_path));
		} catch (IOException ex) {}

		BufferedImage tmp = (BufferedImage) normal;
		
		setBounds(x, y, tmp.getWidth(), tmp.getHeight());
		addMouseListener(this);
		setBorderPainted(false);
		setOpaque(false);
		setVisible(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(onHover ? hover : normal, 0, 0, getWidth(), getHeight(), this);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		onHover = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		onHover = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}
}
