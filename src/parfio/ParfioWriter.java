package parfio;

import java.io.BufferedWriter;
import java.io.Writer;

public class ParfioWriter extends BufferedWriter {

	public ParfioWriter(Writer out){
		super(out);
	}
	
	public ParfioWriter(Writer out, int sz) {
		super(out, sz);
	}

	

}
