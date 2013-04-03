package parfio;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import omp.lc_omp;
import omp.lc_omp.IWork;
import omp.lc_omp.Work;

public class TestOMPMultiFile {

	public static void main(String[] args) throws IOException {
		final long N = 1000000L;
		final long expected = (N * (N + 1) / 2);
		final int THREADS = 10;  //2, 4, 10
		
		
		/* Write */
		createFile(N);  //create 4 text files
		
		/* Specify non-default properties file */
		Parfio.loadProperties("multi_file.Properties");
		
		/* Read w/JOMP */
		long start = System.currentTimeMillis();
		lc_omp.work(new SumWork2(), 1, THREADS);
		long end = System.currentTimeMillis();
		
		/* Results */
		boolean pass = (SumWork2.sum == expected ? true : false);
		System.out.println("JOMP: Pass = " + pass);
		System.out.println("Total time: " + (end - start) + " ms");
		
		/* Clean & exit */
		lc_omp.finish();
		Parfio.close();

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
class SumWork2 implements IWork {
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
		synchronized(this){
			sum += mySum;
		}
		return true;
	}

}