package parfio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class Parfio {
	protected static ArrayList<File> in_file;
	public static ParfioReader stdin;

	static {
		loadProperties("config.Properties");
	}

	
	public static void loadProperties(String config) {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(config));
			in_file = new ArrayList<File>();
			Scanner file_scanner = new Scanner(prop.getProperty("FILE"));
			file_scanner.useDelimiter(",");
			while (file_scanner.hasNext())
				in_file.add(new File(file_scanner.next()));

			file_scanner.close();
			stdin = new ParfioReader(new FileReader(in_file.remove(0)));
		} catch (IOException e) {
			// do nothing for now, throws error when config file doesn't exist
			// or file not found from config.Properties
		}
	}

	public static void close() throws IOException {
		stdin.close();
	}

}
