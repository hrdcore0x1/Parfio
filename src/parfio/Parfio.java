package parfio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

import omp.lc_omp;

public class Parfio {
	private static HashMap<Integer, StreamHandler> hm = new HashMap<Integer, StreamHandler>();

	public static void loadProperties(String config)
			throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(config));

		Scanner file_scanner;
		String input, file;
		int tGroup;
		int idx;
		int tGroupIn;
		int tGroupOut;

		/* grab file config */
		String fileInput = prop.getProperty("FILE");
		if (fileInput != null) {
			file_scanner = new Scanner(fileInput);

			file_scanner.useDelimiter(",");
			while (file_scanner.hasNext()) {
				input = file_scanner.next().trim();
				idx = input.indexOf(" ");
				tGroup = Integer.parseInt(input.substring(0, idx));
				file = input.substring(idx + 1);
				StreamHandler p = (hm.containsKey(tGroup)) ? hm.get(tGroup)
						: new StreamHandler();
				p.openR(new FileReader(file));
				if (!hm.containsKey(tGroup))
					hm.put((Integer) tGroup, p);
			}
			file_scanner.close();
		}
		/* grab outfile config */
		String outInput = prop.getProperty("OUTFILE");
		if (outInput != null){
			file_scanner = new Scanner(outInput);
			file_scanner.useDelimiter(",");
			while (file_scanner.hasNext()) {
				input = file_scanner.next().trim();
				idx = input.indexOf(" ");
				tGroup = Integer.parseInt(input.substring(0, idx));
				file = input.substring(idx + 1);
				StreamHandler p = (hm.containsKey(tGroup)) ? hm.get(tGroup)
						: new StreamHandler();
				p.openW(new FileWriter(file));
				if (!hm.containsKey(tGroup))
					hm.put((Integer) tGroup, p);
			}
		}
		
		
		/* grab pipe config */
		String pipeInput = prop.getProperty("PIPE");
		if (pipeInput != null) {
			file_scanner = new Scanner(pipeInput);
			file_scanner.useDelimiter(",");

			while (file_scanner.hasNext()) {
				// format is "PIPE=inID outID, inID2 outID2, ..., inIDn, outIDn"
				input = file_scanner.next().trim();
				idx = input.indexOf(" ");
				tGroupIn = Integer.parseInt(input.substring(0, idx));
				tGroupOut = Integer.parseInt(input.substring(idx + 1));

				PipedReader pr = new PipedReader(1000000 * Byte.SIZE);
				PipedWriter pw = new PipedWriter(pr);

				StreamHandler pIn = (hm.containsKey(tGroupIn)) ? hm
						.get(tGroupIn) : new StreamHandler();
				StreamHandler pOut = (hm.containsKey(tGroupOut)) ? hm
						.get(tGroupOut) : new StreamHandler();

				pIn.openR(pr);
				pOut.openW(pw);

				System.out.printf("In %d : Out %d\n", tGroupIn, tGroupOut);
				if (!hm.containsKey(tGroupIn))
					hm.put((Integer) tGroupIn, pIn);
				if (!hm.containsKey(tGroupOut))
					hm.put((Integer) tGroupOut, pOut);
			}
			file_scanner.close();
		}
	}

	public static synchronized boolean write(String s) throws IOException {
		int tGroup = lc_omp.omp_get_taskid();
		if (!hm.containsKey(tGroup))
			return false;
		hm.get(tGroup).stdout.write(s);
		return true;
	}

	public static synchronized String readLine() throws IOException {
		int tGroup = lc_omp.omp_get_taskid();
		return (hm.containsKey(tGroup)) ? hm.get(tGroup).stdin.readLine()
				: null;
	}

	public static boolean ready() {
		int tGroup = lc_omp.omp_get_taskid();
		try {
			return (hm.containsKey(tGroup)) ? hm.get(tGroup).stdin.ready()
					: false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static void open(Reader r, int tGroup) {
		if (!hm.containsKey(tGroup))
			return;
		hm.get(tGroup).openR(r);
	}

	public static void open(InputStream is, int tGroup) {
		if (!hm.containsKey(tGroup))
			return;
		hm.get(tGroup).openR(is);
	}

	public static void close() {
		closeIn();
		closeOut();
	}

	private static void closeIn() {
		int tGroup = lc_omp.omp_get_taskid();
		if (!hm.containsKey(tGroup))
			return;
		try {
			hm.get(tGroup).stdin.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void closeOut() {
		int tGroup = lc_omp.omp_get_taskid();
		if (!hm.containsKey(tGroup))
			return;
		try {
			hm.get(tGroup).stdout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
