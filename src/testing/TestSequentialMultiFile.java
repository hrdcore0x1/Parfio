package testing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TestSequentialMultiFile {

	public static void main(String[] args) throws IOException {
		final long N = 1000000L;
		final long expected = (N * (N + 1) / 2);

		/* Write */
		createFile(N);

		long start = System.currentTimeMillis();
		/* Read sequential */
		ArrayList<String> fileNames = new ArrayList<String>();
		fileNames.add("testnums.txt");
		fileNames.add("testnums2.txt");
		fileNames.add("testnums3.txt");
		fileNames.add("testnums4.txt");
		long sum = 0;
		for (String file : fileNames) {
			BufferedReader br = new BufferedReader(new FileReader(file));
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
		}
		long end = System.currentTimeMillis();
		boolean pass = (sum == expected);
		System.out.println("Sequential: Pass = " + pass);
		System.out.println("Total time: " + (end - start) + " ms");
		System.exit(0);
	}

	public static void createFile(long N) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter("testnums.txt"));
		BufferedWriter bw2 = new BufferedWriter(new FileWriter("testnums2.txt"));
		BufferedWriter bw3 = new BufferedWriter(new FileWriter("testnums3.txt"));
		BufferedWriter bw4 = new BufferedWriter(new FileWriter("testnums4.txt"));
		long n = N / 2;
		
		for(long i = 0; i< n; i++){
			if (i <= n/2) bw.write(i + "\n");
			else bw2.write(i + "\n");
		}
		for (long i = n; i <= N; i++) {
			if (i <= N/2) bw3.write(i + "\n");
			else bw4.write(i + "\n");
		}
		bw.close();
		bw2.close();
		bw3.close();
		bw4.close();
	}
}
