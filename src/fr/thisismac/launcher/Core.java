package fr.thisismac.launcher;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import fr.theshark34.openauth.AuthPoints;
import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openauth.Authenticator;
import fr.theshark34.openauth.model.AuthAgent;
import fr.theshark34.openauth.model.response.AuthResponse;
import fr.thisismac.core.Start;
import fr.thisismac.launcher.elements.CustomBackground;
import fr.thisismac.launcher.elements.CustomBar;
import fr.thisismac.launcher.elements.CustomButton;
import fr.thisismac.launcher.elements.CustomImage;
import fr.thisismac.launcher.elements.CustomPasswordField;
import fr.thisismac.launcher.elements.CustomTextField;
import fr.thisismac.launcher.elements.OptionsFrame;
import fr.thisismac.launcher.elements.UpdaterThread;
import fr.thisismac.launcher.java.JavaProcessLauncher;
import fr.thisismac.launcher.utils.ConfigUtils;
import fr.thisismac.launcher.utils.EncryptUtils;
import fr.thisismac.launcher.utils.HashUtils;
import fr.thisismac.launcher.utils.DirUtils.OS;
import lombok.Getter;
import fr.thisismac.launcher.utils.DirUtils;

public class Core extends JFrame implements KeyListener, MouseListener {

	/* Initialisation de l'instance */
	private static Core instance;
	public static Core get() {
		return instance;
	}
	
	/* Initialisation de la font */
	@Getter public Font font = Font.getFont("Arial");
	
	/*   Initialisation des composents graphique*/
	@Getter final OptionsFrame optionsFrame = new OptionsFrame();
	@Getter CustomTextField user = new CustomTextField(75, 230, "/field.png");
	@Getter CustomPasswordField password = new CustomPasswordField(75, 280, "/field.png");
	@Getter CustomButton quit = new CustomButton(860, 7, "/close_normal.png", "/close_hover.png");
	@Getter CustomButton reduce = new CustomButton(820, 7, "/reduce_normal.png", "/reduce_hover.png");
	@Getter CustomButton options = new CustomButton(780, 7, "/options_normal.png", "/options_hover.png");
	@Getter CustomBar bar = new CustomBar(350, 483, "/progress_full.png");
	@Getter CustomImage play = new CustomImage(100, 350, "/connexion.png");
	@Getter CustomImage forgot = new CustomImage(200, 480, "/forgot_pwd.png");
	@Getter CustomImage register = new CustomImage(10, 480, "/register.png");
	
	/* Initialisation de l'auth et de l'updater */
	@Getter final AuthPoints points = new AuthPoints("authenticate", "refresh", "validate", "signout", "invalidate");
	@Getter final Authenticator authenticator = new Authenticator("http://auth.obsifight.fr/", points);
	@Getter AuthResponse response;
	
	// Variable random
	private Point initialClick;
	@Getter boolean debug;

	
	public Core(boolean debug) {
		instance = this;
		this.debug = debug;
		optionsFrame.setFocus(false);
		
		setSize(ConfigUtils.SIZE_X, ConfigUtils.SIZE_Y);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		setUndecorated(true);
		addKeyListener(this);
		setFocusTraversalKeysEnabled(false);
		setFocusable(true);
		
		 try { 
			 setIconImage(ImageIO.read(Core.class.getResource("/favicon.png")));
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				initialClick = e.getPoint();
				getComponentAt(initialClick);
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int thisX = getLocation().x;
				int thisY = getLocation().y;

				int xMoved = (thisX + e.getX()) - (thisX + initialClick.x);
				int yMoved = (thisY + e.getY()) - (thisY + initialClick.y);

				int X = thisX + xMoved;
				int Y = thisY + yMoved;
				setLocation(X, Y);
			}
		});
		
		quit.setOpaque(false);
		reduce.setOpaque(false);
		options.setOpaque(false);
		
		user.addKeyListener(this);
		password.addKeyListener(this);
		
		reduce.addMouseListener(this);
		options.addMouseListener(this);
		quit.addMouseListener(this);
		play.addMouseListener(this);
		forgot.addMouseListener(this);
		register.addMouseListener(this);
		
		bar.setOpaque(false);
		
		user.setFont(font);
		password.setFont(font);
		bar.setFont(font);
		
		
		CustomBackground bg = new CustomBackground();
		bg.setBounds(0, 0, ConfigUtils.SIZE_X, ConfigUtils.SIZE_Y);

		add(user);
		add(password);
		add(quit);
		add(reduce);
		add(play);
		add(options);
		add(bar);
		add(forgot);
		add(register);
		add(bg);

		EncryptUtils.readLastLogin(user, password);
		setVisible(true);
	}

	public void play() {
			System.out.println("Calling AUTH for user : " + user.getText());
			
			String reason = null;
			try {
				response = authenticator.authenticate(AuthAgent.MINECRAFT, user.getText(), password.getText(), null);
			} catch (AuthenticationException e) {
				reason = e.getErrorModel().getErrorMessage();
			}
						
			if (response == null) {
				JOptionPane.showMessageDialog(new Frame(), reason, "Problème d'authentification !", 0);
				password.setText("");
			} 
			else {
				if(optionsFrame.isRememberChecked()) {
					EncryptUtils.saveLastLogin(user.getText(), password.getText());
				}
				else if(EncryptUtils.lastlogin.exists()){
					EncryptUtils.lastlogin.delete();
				}
				user.setEditable(false);
				password.setEditable(false);
				new UpdaterThread().start();
			}
	}
	
	public void launchGame(){
		File[] libs = new File(DirUtils.getWorkingDirectory(), "bin" + File.separator + "libs").listFiles();
		StringBuilder strBuilder = new StringBuilder();
		for(File file : libs) {
			strBuilder.append(file.getAbsolutePath() + (DirUtils.getPlatform() == OS.windows ? ";" : ":"));
		}
		
		File tmp = new File(DirUtils.getPath() + File.separator + "assets", "skins");
		if (!tmp.exists())
			tmp.mkdir();
		JavaProcessLauncher processLauncher = new JavaProcessLauncher(null, 
				new String[] {
				  "-Xms512m" , 
				  "-Xmx" + Core.get().getOptionsFrame().getSelectedRAM() ,
				  "-Dlog4j.configurationFile=" + DirUtils.getPath() + "" + File.separator + "log4j2.xml",
				  "-Djava.library.path=" + DirUtils.getPath() + "" + File.separator + "bin" + File.separator + "natives" + File.separator,
				  "-cp", strBuilder.toString() + DirUtils.getPath() + "" + File.separator + "bin" + File.separator + "obsifight.jar",
				  "net.minecraft.client.main.Main",
				  "--username", Core.get().getResponse().getSelectedProfile().getName(),
				  "--version", "1.8",
				  "--gameDir", DirUtils.getWorkingDirectory().getAbsolutePath(),
				  "--assetIndex", "1.8",
				  "--assetsDir", DirUtils.getPath() + File.separator + "assets",
				  "--uuid", Core.get().getResponse().getSelectedProfile().getId(),
				  "--userProperties", "{}",
				  "--accessToken", Core.get().getResponse().getAccessToken()});
		processLauncher.directory(DirUtils.getWorkingDirectory());
		try {
			processLauncher.start();
			
			//if (!debug)
			// System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER && user.getText().length() > 0 && !bar.isDownload()) {
			play();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == play && user.getText().length() > 0 && !bar.isDownload()) {
			play();
		} 
		else if (e.getSource() == quit) {
			System.exit(0);
		}
		else if (e.getSource() == reduce) {
			setState(JFrame.ICONIFIED);
		} 
		else if(e.getSource() == options) {
			optionsFrame.setFocus(true);
		}
		else if(e.getSource() == forgot) {
			try {
				Desktop.getDesktop().browse(new URI("http://obsifight.fr/connexion"));
			} catch (IOException e1) {} catch (URISyntaxException e1) {}
		}
		else if(e.getSource() == register) {
			try {
				Desktop.getDesktop().browse(new URI("http://obsifight.fr/obsifight/user/signup"));
			} catch (IOException e1) {} catch (URISyntaxException e1) {}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(e.getSource() instanceof CustomImage) {
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(e.getSource() instanceof CustomImage) {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
}
