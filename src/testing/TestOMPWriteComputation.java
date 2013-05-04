package testing;

import java.io.IOException;

import omp.lc_omp;
import omp.lc_omp.IWork;
import omp.lc_omp.Work;

import parfio.Parfio;

public class TestOMPWriteComputation {

	public static void main(String[] args) throws IOException {
		final long N = 1000000L; // 1000000L;
		final long expected = (N * (N + 1) / 2);
		final int THREADS = 4;

		long start = System.currentTimeMillis();
		Parfio.loadProperties("write.Properties");
		lc_omp.work(new _WriteWork(), 0, (int) N, 1, 1, THREADS);
		long end = System.currentTimeMillis();

		lc_omp.work(new _ReadWork(), 1, 4);

		boolean pass = (_ReadWork.sum == expected);
		System.out.println("JOMP: Passs = " + pass);
		System.out.println("Total time (Writing): " + (end - start) + " ms");
		Parfio.close();
		lc_omp.finish();

	}

}

class _WriteWork implements lc_omp.IWork {

	@Override
	public boolean evaluate(Work w) {
		for (int i = w.start; i <= w.end; i++) {
			try {
				String line = i + "\r\n";
				Parfio.write(line);
				for (int j = 0, k = 0; j < 100; j++)
					k *= j * i * Integer.parseInt(i + "");
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		return true;
	}

}

class _ReadWork implements IWork {
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
