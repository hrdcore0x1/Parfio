package testing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TestSequentialWriteComputation {

	public static void main(String[] args) throws IOException {
		final long N = 1000000L;
		final long expected = (N * (N + 1) / 2);

		/* Write */
		long start = System.currentTimeMillis();
		createFile(N);
		long end = System.currentTimeMillis();
		
		/* Read sequential */
		BufferedReader br = new BufferedReader(new FileReader("testnums.txt"));
		long sum = 0;
		for (;;) {
			String line = null;
			try {
				line = br.readLine();
			} catch (Exception ex) {
			}
			if (line == null)
				break;
			sum += Integer.parseInt(line);
		}
		br.close();
		
		boolean pass = (sum == expected);
		System.out.println("Sequential: Pass = " + pass);
		System.out.println("Total time (Writing): " + (end - start) + " ms");
		System.exit(0);
	}

	public static void createFile(long N) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter("testnums.txt"));
		for (int i = 0; i <= N; i++) {
			String line = i + "\r\n";
			bw.write(line);
			for (int j = 0, k = 0; j < 100; j++)
				k *= j * i * Integer.parseInt(i + "");
		}
		bw.close();
	}
}


