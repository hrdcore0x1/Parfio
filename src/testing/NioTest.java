package testing;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;

public class NioTest {

	public NioTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		FileChannel fc = FileChannel.open(Paths.get("testnums.txt"), java.nio.file.StandardOpenOption.READ);
		
		
		int pos = 0;
		int size = (int) fc.size();
		int chunk = 2;

		int sum = 0;
		while ( pos <= size ){
			byte[] b = new byte[chunk];
			ByteBuffer bb = ByteBuffer.wrap(b);
			fc.read(bb, pos);
			String s = new String(bb.array());
			System.out.println(s);
			try{
			sum += Integer.parseInt(s);
			}catch(Exception e){}
			pos += chunk;
		}
		System.out.println("SUM: " + sum);

	}

}
