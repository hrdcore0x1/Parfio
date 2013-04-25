package testing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import omp.lc_omp;
import omp.lc_omp.IWork;
import omp.lc_omp.Work;
import parfio.Parfio;

public class TestOMP {

	public static void main(String[] args) throws IOException {
		final long N = 1000000L; // 1000000L;
		final long expected = (N * (N + 1) / 2);
		final int THREADS = 4;

		/* Write */
		createFile(N);

		/* Specify nondefault config file to load */
		Parfio.loadProperties("single_file.Properties");

		/* Read w/JOMP */
		long start = System.currentTimeMillis();
		lc_omp.work(new SumWork(), 1, THREADS);
		long end = System.currentTimeMillis();

		/* Results */
		boolean pass = (SumWork.sum == expected);
		System.out.println("JOMP: Pass = " + pass);
		System.out.println("Total time: " + (end - start) + " ms");

		/* Clean & exit */
		lc_omp.finish();
	}

	public static void createFile(long N) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter("testnums.txt"));
		for (int i = 0; i <= N; i++) {
			bw.write(i + "\n");
		}
		bw.close();
	}
}

class SumWork implements IWork {
	public static long sum = 0;

	@Override
	public boolean evaluate(Work w) {
		String line = null;
		long mySum = 0;
		for (;;) {
			try {
				line = Parfio.readLine();
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