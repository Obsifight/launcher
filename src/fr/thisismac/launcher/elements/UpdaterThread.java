package fr.thisismac.launcher.elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import fr.thisismac.launcher.Core;
import fr.thisismac.launcher.utils.ConfigUtils;
import fr.thisismac.launcher.utils.DirUtils;
import fr.thisismac.launcher.utils.XMLUtils;
import lombok.Setter;

public class UpdaterThread extends Thread {

	@Setter
	List<CustomFile> toDownload = new ArrayList<CustomFile>();

	@Override
	public void run() {

		// Check integrity of all file
		Core.get().getBar().setDisplayMessage("Vérification de mise-à-jour 1/2");
		//XMLUtils.prepareJars(toDownload);
		Core.get().getBar().repaint();
		
		Core.get().getBar().setDisplayMessage("Vérification de mise-à-jour 2/2");
		XMLUtils.prepareRessources(toDownload);
		Core.get().getBar().repaint();

		// If no file need to be updated launch the game
		if (toDownload.size() == 0) {
			Core.get().launchGame();
			return;
		}

		// Else download them
		int tmp = 0;
		for (CustomFile file : toDownload)
			tmp += file.getSize();

		Core.get().getBar().setMaximum_size(tmp);
		Core.get().getBar().setFile_left(toDownload.size());

		Core.get().getBar().setDownload(true);

		ExecutorService pool = Executors.newFixedThreadPool(10);
		for (CustomFile file : toDownload) {
			pool.submit(new DownloadTask(file));
		}

		while (Core.get().getBar().getFile_left() > 0) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		Core.get().getBar().setDownload(false);
		Core.get().getBar().setDisplayMessage("Lancement de " + ConfigUtils.NAME);
		Core.get().getBar().repaint();

		Core.get().launchGame();
	}
}