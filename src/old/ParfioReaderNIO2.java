package old;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class ParfioReaderNIO2 {

	private FileChannel fc;
	private int pos;
	private final int CHUNK_SIZE = 255;
	private String leftOver;
	private ByteBuffer bb;

	public ParfioReaderNIO2(String fileName) {
		pos = 0;
		leftOver = "";
		bb = ByteBuffer.allocate(CHUNK_SIZE);
		try {
			fc = FileChannel.open(Paths.get(fileName),
					java.nio.file.StandardOpenOption.READ);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public String read(){
		try {
			if (pos > fc.size()) return null;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] bytes = new byte[1];
		bb = ByteBuffer.wrap(bytes);
		try {
			fc.read(bb,pos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pos += 1;
		String ret = new String(bytes);
		if (ret.contains("\n") || ret.contains("\r")) return "";
		return ret + read();
		
		
	}
	
	public String readLine() {
		String ret = readLeftOver();
		
		bb.clear();
		try {
			fc.read(bb, pos);
			pos += bb.capacity();
		} catch (Exception e) {	}
	
		String s = new String(bb.array());
		ret = leftOver;
		boolean over = false;
		for(char c : s.toCharArray()){
			if (c == '\n' || c == '\r'){
				over = true;
			}
			if (over){
				leftOver += c;
			}else{
				ret += c;
			}
		}
		return ret;
	}
	
	private String readLeftOver(){
		String ret = "";
		for(char c : leftOver.toCharArray()){
			if (c == '\n' || c == '\r') break;
			ret += c;
			
		}
		
		return null;
	}

}
