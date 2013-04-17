package parfio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class ParfioReaderNIO3 {

	private FileChannel fc;
	private int pos;
	private String leftOver;
	private ByteBuffer bb;
	private ArrayList<String> buffer;
	private int CHUNK = 2;
	Object lock;

	public ParfioReaderNIO3(String fileName) {
		pos = 0;
		leftOver = "";
		try {
			fc = FileChannel.open(Paths.get(fileName),
					java.nio.file.StandardOpenOption.READ);
			CHUNK = (int) fc.size();
		} catch (IOException e) {
			System.out.println(e);
		}

		buffer = new ArrayList<String>();
		lock = new Object();
		try {
			fill();
		} catch (Exception e) {
		}

	}

	public synchronized boolean fill() throws IOException {

		try {
			if (pos > fc.size())
				return false;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] bytes = new byte[CHUNK];
		bb = ByteBuffer.wrap(bytes);
		try {
			// System.out.println(pos + " - " + (pos + CHUNK));
			fc.read(bb, pos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pos += CHUNK;

		String tmp = new String(bytes);
		if (tmp.charAt(0) == '\n')
			tmp = tmp.substring(1);
		tmp = leftOver + tmp;
		Scanner s = new Scanner(tmp);
		s.useDelimiter("\n");
		boolean first = true;
		while (s.hasNext()) {
			if (first) {
				first = !first;
				leftOver = leftOver + s.next();
			} else
				leftOver = s.next();
			buffer.add(leftOver);
			// System.out.println("adding " + leftOver);
		}
		s.close();
		// retain leftover and don't add to buffer
		if (tmp.charAt(tmp.length() - 1) == '\n' && buffer.size() > 0)
			leftOver = "";

		/*
		 * while (leftOver.charAt(leftOver.length() - 1) != '\n' && pos <
		 * fc.size()) { byte[] b = new byte[leftOver.getBytes().length]; int i =
		 * 0; for (byte by : leftOver.getBytes()) { b[i] = by; i++; }
		 * 
		 * b[leftOver.getBytes().length - 1] = getByte(); leftOver = new
		 * String(b); System.out.println("LEFTOVER: " + leftOver); }
		 */

		if (buffer.size() > 0)
			return true;
		return false;
	}

	private byte getByte() {
		try {
			ByteBuffer bb = ByteBuffer.allocate(1);
			fc.read(bb, pos);

			pos++;
			return bb.get(0);
		} catch (Exception e) {
			return -1;
		}
	}

	public synchronized String readLine() {
		if (buffer.size() > 0)
			return buffer.remove(0);
		try {
			if (fill()) {
				return readLine();
			}
		} catch (Exception e) {
		}
		return null;
	}

}
