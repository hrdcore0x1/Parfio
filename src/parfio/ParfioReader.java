package parfio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


public class ParfioReader extends BufferedReader {


	public ParfioReader(Reader in) {
		super(in);
	}
	

	public synchronized String readLine() throws IOException{
		
		/* check if first call to readLine(), if so open first file...*/
		/*
		if (first && Parfio.in_file.size() > 0){
			first = !first;
			File nFile = Parfio.in_file.remove(0);
			System.out.println("nFile: " + nFile);
			Parfio.stdin = new ParfioReader(new FileReader(nFile));
			System.out.println("Opening init file");
			return Parfio.stdin.readLine();
		}*/
		
		String line = super.readLine();
		
		/* check if more files exists when line=null, if so open next file */
		if (line == null && Parfio.in_file.size() > 0) {
			Parfio.stdin.close();
			Parfio.stdin = new ParfioReader(new FileReader(Parfio.in_file.remove(0)));
			System.out.println("Opening new file2");
			return Parfio.stdin.readLine();
		}
		
		System.out.println("Returning line");
		return line;
	}

}
