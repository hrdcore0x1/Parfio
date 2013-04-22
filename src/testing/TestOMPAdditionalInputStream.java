package testing;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import omp.lc_omp;
import omp.lc_omp.IWork;
import omp.lc_omp.Work;
import parfio.Parfio;

public class TestOMPAdditionalInputStream {

	public static void main(String[] args) throws IOException {
		final long N = 1000000L; // 1000000L;
		int n = 1000;
		final long expected = (N * (N + 1) / 2);
		final long totalExpected = (n *(n + 1) / 2) + expected;
		final int THREADS = 4;

		/* Write */
		createFile(N, "testnums.txt");
		createFile(n, "testadd.txt");
		
		
		
		

		/* Specify nondefault config file to load */
		Parfio.loadProperties("single_file.Properties");

		
		/* Read w/JOMP */
		long start = System.currentTimeMillis();
		lc_omp.work(new SumWork4(), 1, THREADS);
		Parfio.open(new FileInputStream("testadd.txt"));  //add new file to list of input streams
		lc_omp.work(new SumWork4(), 1, THREADS);
		long end = System.currentTimeMillis();

		/* Results */
		boolean pass = (SumWork4.sum == totalExpected);
		System.out.println("JOMP: Pass = " + pass);
		System.out.println("Total time: " + (end - start) + " ms");

		/* Clean & exit */
		lc_omp.finish();
		Parfio.stdin.close();

	}

	public static void createFile(long N, String filename) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
		for (int i = 0; i <= N; i++) {
			bw.write(i + "\n");
		}
		bw.close();
	}
}

class SumWork4 implements IWork {
	public static long sum = 0;

	@Override
	public boolean evaluate(Work w) {
		String line = null;
		long mySum = 0;
		for (;;) {
			try {
				line = Parfio.stdin.readLine();
			} catch (Exception ex) {
			}
			if (line == null)
				break;
			mySum += Integer.parseInt(line);
		}
		synchronized (this) {
			sum += mySum;
		}
		return true;
	}

}