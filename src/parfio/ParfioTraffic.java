package parfio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

import omp.lc_omp;

public class ParfioTraffic {
	private static HashMap<Integer, Parfio2> hm = new HashMap<Integer, Parfio2>();
	
	
	public static void loadSettings(String config) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(config));
		Scanner file_scanner = new Scanner(prop.getProperty("FILE"));
		String input, file;
		int tGroup;
		file_scanner.useDelimiter(",");
		while (file_scanner.hasNext()){
			input = file_scanner.next().trim();
			tGroup = Integer.parseInt(input.substring(0, input.indexOf(" ")));
			file = input.replace(String.valueOf(tGroup), "");
			Parfio2 p = (hm.containsKey(tGroup)) ? hm.get(tGroup) : new Parfio2();
			p.open(new FileReader(file));
			
			if (!hm.containsKey(tGroup)) hm.put((Integer)tGroup, p);	
		}
		file_scanner.close();
		
		String outfile = prop.getProperty("OUTFILE");

	}

	
	public static synchronized String readLine() throws IOException{
		int tGroup = lc_omp.omp_get_taskid();
		
		return (hm.containsKey(tGroup)) ? hm.get(tGroup).stdin.readLine() : null;
	}


}
