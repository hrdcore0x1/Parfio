package parfio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;



public class ParfioReader extends BufferedReader {

	public ParfioReader(Reader in) {
		super(in);
	}
	
	public String readLine() throws IOException{
		
		
		return super.readLine();
		
	}
	

}
