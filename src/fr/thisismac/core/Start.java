package fr.thisismac.core;

import fr.thisismac.launcher.Core;

public class Start {
	
	public Start() {
		new Core(false);
	}
	
	
	public static void main(String[] args) {
		boolean tmp = false;
		
		for (String arg : args) {
			if (arg.equals("debug"))
				tmp = true;
				
		}
		new Core(tmp);
    }
}