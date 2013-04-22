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
		String line = super.readLine();
		
		/* check if more files exists when line=null, if so open next file */
		if (line == null && Parfio.in_file.size() > 0) {
			Parfio.stdin.close();
			Parfio.stdin = new ParfioReader(Parfio.in_file.remove(0));
			return Parfio.stdin.readLine();
		}
		
		return line;
	}

}
