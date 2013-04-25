package parfio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;


public class ParfioReader extends BufferedReader {
	private StreamHandler p;

	public ParfioReader(Reader in, StreamHandler p) {
		super(in);
		this.p = p;
	}
	
	public synchronized String readLine() throws IOException{
		String line = super.readLine();
		
		/* check if more files exists when line=null, if so open next file */
		if (line == null && p.in_file.size() > 0) {
			p.stdin.close();
			p.stdin = new ParfioReader(p.in_file.remove(0), p); 
			return p.stdin.readLine();
		}
		
		return line;
	}

}
