package parfio;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;


public class StreamHandler {
	public ArrayList<Reader> in_file = new ArrayList<Reader>();
	public ParfioReader stdin;
	public ParfioWriter stdout;
	private boolean first = true;
	
	
	private void checkFirst(){
		if (first){
			first = !first;
			stdin = new ParfioReader(in_file.remove(0), this);
		}
	}
	
	public void openR(Reader r){
		in_file.add(r);
		checkFirst();
	}
	
	public void openR(InputStream is){
		in_file.add(new InputStreamReader(is));
		checkFirst();
	}
	
	public void openW(Writer w){
		stdout = new ParfioWriter(w);
	}
	
	public void nextR(){
		try {
			stdin.close();
			if (in_file.size() > 0){
				stdin = new ParfioReader(in_file.remove(0), this);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
