package fr.thisismac.launcher.elements;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import fr.thisismac.launcher.utils.DirUtils;

public class OptionsFrame extends JFrame implements ActionListener{


	private final File optionsFile = new File(DirUtils.getWorkingDirectory() + File.separator + "launcherOptions.txt");
	private JLabel remember = new JLabel("Se souvenir des identifiants ?");
	private JLabel ram = new JLabel("Allouage de la RAM :");
	private JButton ok = new JButton("Valider");
	
    private final JComboBox ramChoice = new JComboBox(new String[] {"512m", "1024m","2048m", "3072m","4096m"});
	private JCheckBox rememberBox = new JCheckBox();

	
	public OptionsFrame() {
		
		setSize(250, 170);
		setResizable(false);
		setLayout(null);
		setLocationRelativeTo(null);
		setTitle("Options du Launcher");
		
		remember.setForeground(Color.black);
		remember.setBounds(30, 0, 200, 70);
		
		rememberBox.setBounds(200, 25, 20, 20);
		
		ram.setBounds(30, 35, 200, 70);
		ramChoice.setBounds(150, 60, 70, 25);
		ramChoice.setSelectedIndex(0);
		
		ok.setBounds(75, 100, 100, 30);
		ok.addActionListener(this);
		
		getSaveOptions();
		saveOptions();
		
		add(remember);
		add(rememberBox);

		add(ram);
		add(ramChoice);
		add(ok);
		
		setVisible(false);
		
		addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent windowEvent) {
		       setFocus(false);
		       saveOptions();
		    }
		});
	}

	private void saveOptions() {
		if(!optionsFile.exists()) { 
			try {
				optionsFile.createNewFile();
			} catch (IOException e) {} 
		}
		
		try {
			BufferedWriter bf = new BufferedWriter(new FileWriter(optionsFile));
			
			bf.write(rememberBox.isSelected() + ":" + ramChoice.getSelectedItem().toString());
			bf.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	

	public void getSaveOptions() {
		
		if(!optionsFile.exists()) { return; }
		
		try {
			BufferedReader bf = new BufferedReader(new FileReader(optionsFile));
			String[] options = bf.readLine().split(":");
			
			if(options[0].contains("true")) {
				rememberBox.setSelected(true);
			}
			ramChoice.setSelectedItem(options[1]);
			bf.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == ok) {
			saveOptions();
			setFocus(false);
		}
	}
	
	public String getSelectedRAM() {
		return ramChoice.getSelectedItem().toString();
	}
	
	public boolean isRememberChecked() {
		return rememberBox.isSelected();
	}
	
	public void setFocus(boolean bool) {
		if (bool) {
			this.setVisible(true);
			this.setAlwaysOnTop(true);
		}
		else {
			this.setVisible(false);
			this.setAlwaysOnTop(false);
		}
	}
	

}
