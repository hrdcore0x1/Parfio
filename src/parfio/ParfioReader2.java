package parfio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedWriter;
import java.io.Reader;


public class ParfioReader2 extends BufferedReader {
	private Parfio2 p;

	public ParfioReader2(Reader in, Parfio2 p) {
		super(in);
		this.p = p;
	}
	
	public synchronized String readLine() throws IOException{
		String line = super.readLine();
		
		/* check if more files exists when line=null, if so open next file */
		if (line == null && Parfio.in_file.size() > 0) {
			p.stdin.close();
			p.stdin = new ParfioReader2(p.in_file.remove(0), p); 
			return p.stdin.readLine();
		}
		
		return line;
	}

}
