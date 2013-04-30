package testing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class TestSequentialPipe2 {

	public static void main(String[] args) throws IOException {
		final long N = 1000000;
		final long expected = (N * (N + 1) / 2) * 2;

		PipedReader pr = new PipedReader((int)N * Byte.SIZE);
		PipedWriter pw = new PipedWriter(pr);
		PipedReader pr2 = new PipedReader((int)N * Byte.SIZE);
		PipedWriter pw2 = new PipedWriter(pr2);
		
		long start = System.currentTimeMillis();
		/* Write */
		for(int i=0; i<=N; i++){
			pw.write(i + "\n");
		}
		
		

		
		/* Read sequential */
		long sum = 0;
		BufferedReader br = new BufferedReader(pr);
		for (;br.ready();) {
			String line = null;
			try {
				line = br.readLine();
				pw2.write(line + "\n");
			} catch (Exception ex) {
			}
			if (line == null)
				break;
			sum += Integer.parseInt(line);
		}
		br.close();
		pw.close();
		br = new BufferedReader(pr2);
		for (;br.ready();) {
			String line = null;
			try {
				line = br.readLine();
			} catch (Exception ex) {
			}
			if (line == null)
				break;
			sum += Integer.parseInt(line);
		}
		
		long end = System.currentTimeMillis();
		boolean pass = (sum == expected);
		System.out.println("Sequential: Pass = " + pass);
		System.out.println("Total time: " + (end - start) + " ms");
		System.exit(0);
	}

}


