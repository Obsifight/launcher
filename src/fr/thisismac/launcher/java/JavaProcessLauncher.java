package fr.thisismac.launcher.java;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaProcessLauncher {
	private final String jvmPath;
	private final List<String> commands;
	private File directory;

	public JavaProcessLauncher(String jvmPath, String[] commands) {
		if (jvmPath == null)
			jvmPath = getJavaDir();
		this.jvmPath = jvmPath;
		this.commands = Arrays.asList(commands);
	}

	public JavaProcess start() throws IOException {
		List<String> full = getFullCommands();
		System.out.println(full);
		return new JavaProcess(full, new ProcessBuilder(full).directory(this.directory).redirectErrorStream(true).start());
	}

	public List<String> getFullCommands() {
		List<String> result = new ArrayList<String>(commands);
		result.add(0, getJavaPath());
		return result;
	}

	public List<String> getCommands() {
		return this.commands;
	}

	public void addCommands(String[] commands) {
		this.commands.addAll(Arrays.asList(commands));
	}

	public void addSplitCommands(String commands) {
		addCommands(commands.split(" "));
	}

	public JavaProcessLauncher directory(File directory) {
		this.directory = directory;

		return this;
	}

	public File getDirectory() {
		return this.directory;
	}

	protected String getJavaPath() {
		return this.jvmPath;
	}

	public static String getJavaDir() {
		String separator = System.getProperty("file.separator");
		String path = System.getProperty("java.home") + separator + "bin"
				+ separator;

		if ((getOs() == EnumOS.WINDOWS) && (new File(path + "javaw.exe").isFile())) {
			return path + "javaw.exe";
		}

		return path + "java";
	}

	public static EnumOS getOs() {
		String var0 = System.getProperty("os.name").toLowerCase();
		return var0.contains("win") ? EnumOS.WINDOWS
				: (var0.contains("mac") ? EnumOS.MACOS : (var0
						.contains("solaris") ? EnumOS.SOLARIS : (var0
						.contains("sunos") ? EnumOS.SOLARIS : (var0
						.contains("linux") ? EnumOS.LINUX : (var0
						.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN)))));
	}

	public enum EnumOS {
		LINUX, SOLARIS, WINDOWS, MACOS, UNKNOWN;
	}

	public String toString() {
		return "JavaProcessLauncher[commands=" + this.commands + ", java="
				+ this.jvmPath + "]";
	}
}