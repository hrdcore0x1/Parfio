package parfio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class ParfioNIO {
	private static  ArrayList<File> in_file;
	public static ParfioReaderNIO3 stdin;
/*
	static {
		loadProperties("config.Properties");
	}
*/
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
			stdin = new ParfioReaderNIO3(in_file.remove(0).toString());
		} catch (IOException e) {
			// do nothing for now, throws error when config file doesn't exist
			// or file not found from config.Properties
		}
	}

	
	protected static synchronized File popFile(){
		if (in_file.size() == 0) return null;
		return in_file.remove(0);
	}

}
