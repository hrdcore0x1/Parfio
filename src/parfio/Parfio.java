package parfio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;


public class Parfio {
	public static ArrayList<Reader> in_file;
	public static ParfioReader stdin;
	public static ParfioWriter stdout;

	
	public static void loadProperties(String config) {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(config));
			in_file = new ArrayList<Reader>();
			Scanner file_scanner = new Scanner(prop.getProperty("FILE"));
			file_scanner.useDelimiter(",");
			while (file_scanner.hasNext())
				in_file.add(new FileReader(new File(file_scanner.next())));
			file_scanner.close();
			
			stdin = new ParfioReader(in_file.remove(0));
			String outfile = prop.getProperty("OUTFILE");
			try{
			stdout = new ParfioWriter(new FileWriter(new File(outfile)));
			}catch(Exception e){
				stdout = null;
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	/* Appends the input stream to the in_file arraylist */
	
	public static void open(InputStream is){
		in_file.add(new InputStreamReader(is));
	}



}
