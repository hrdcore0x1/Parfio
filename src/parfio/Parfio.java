package parfio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;


public class Parfio {
	public static ArrayList<File> in_file;
	public static ParfioReader stdin;
	public static ParfioWriter stdout;


	
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
			/* Initialize the obj so it exist.  ParfioReader will check for the first readLine() method and then open the first file.*/
			/* This is so that we can write to a file then read it back */
			
			stdin = new ParfioReader(new FileReader(in_file.remove(0)));
			String outfile = prop.getProperty("OUTFILE");
			try{
			stdout = new ParfioWriter(new FileWriter(new File(outfile)));
			}catch(Exception e){
				stdout = null;
			}
		} catch (IOException e) {
			System.out.println(e);
			// do nothing for now, throws error when config file doesn't exist
			// or file not found from config.Properties
		}
	}

	public static void close() throws IOException {
		stdin.close();
	}

}
