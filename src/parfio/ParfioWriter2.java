package parfio;

import java.io.BufferedWriter;
import java.io.Writer;

public class ParfioWriter2 extends BufferedWriter {

	public ParfioWriter2(Writer out){
		super(out);
	}
	
	public ParfioWriter2(Writer out, int sz) {
		super(out, sz);
	}

}
