package parfio;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

public class ParfioWriter extends BufferedWriter {

	public ParfioWriter(Writer out){
		super(out);
	}
	
	public ParfioWriter(Writer out, int sz) {
		super(out, sz);
	}

	public void write(String s) throws IOException{
		super.write(s);
		super.flush();
	}
}
