package parfio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;


public class Parfio2 {
	public ArrayList<Reader> in_file;
	public ParfioReader2 stdin;
	public ParfioWriter2 stdout;
	
	private PipedReader pr;
	private PipedWriter pw;
	
	private boolean first = true;
	
	/*public void loadProperties(String config) {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(config));
			in_file = new ArrayList<Reader>();
			Scanner file_scanner = new Scanner(prop.getProperty("FILE"));
			
			file_scanner.useDelimiter(",");
			while (file_scanner.hasNext())
				in_file.add(new FileReader(new File(file_scanner.next())));
			file_scanner.close();
			
			stdin = new ParfioReader2(in_file.remove(0), this);
			String outfile = prop.getProperty("OUTFILE");
			try{
			stdout = new ParfioWriter2(new FileWriter(new File(outfile)));
			}catch(Exception e){
				stdout = null;
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	*/
	/* Appends the input stream to the in_file arraylist */
	
	public void open(Reader r){
		in_file.add(r);
		
	}
	
	public void open(InputStream is){
		in_file.add(new InputStreamReader(is));
	}
	

	


}
