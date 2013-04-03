package parfio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TestSequential {

	public static void main(String[] args) throws IOException {
		final long N = 1000000L;
		final long expected = (N * (N + 1) / 2);

		/* Write */
		createFile(N);

		long start = System.currentTimeMillis();
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
		long end = System.currentTimeMillis();
		boolean pass = (sum == expected ? true : false);
		System.out.println("Sequential: Pass = " + pass);
		System.out.println("Total time: " + (end - start) + " ms");
		System.exit(0);
	}

	public static void createFile(long N) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter("testnums.txt"));
		for (int i = 0; i <= N; i++) {
			bw.write(i + "\n");
		}
		bw.close();
	}
}


