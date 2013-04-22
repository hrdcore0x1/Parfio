package testing;

import java.io.IOException;

import omp.lc_omp;
import omp.lc_omp.IWork;
import omp.lc_omp.Work;

import parfio.Parfio;

public class TestOMPWrite {

	public static void main(String[] args) throws IOException {
		final long N = 1000000L; // 1000000L;
		final long expected = (N * (N + 1) / 2);
		final int THREADS = 4;
 
		long start = System.currentTimeMillis();
		Parfio.loadProperties("config.Properties");
		lc_omp.work(new WriteWork(), 0, (int)N, 1, 1, THREADS);
		Parfio.stdout.close();
		long end = System.currentTimeMillis();
		
		lc_omp.work(new ReadWork(), 2, 4);
		Parfio.stdin.close();
		
		boolean pass = (ReadWork.sum == expected);
		System.out.println("JOMP: Pass = " + pass);
		System.out.println("Total time (Writing): " + (end - start) + " ms");
		lc_omp.finish();
		
	}

}

class WriteWork implements lc_omp.IWork{

	@Override
	public boolean evaluate(Work w) {
		for(int i=w.start; i<=w.end; i++){
			try{
			Parfio.stdout.write(i + "\r\n");
			}catch(Exception e){
				System.out.println(e);
				break;
			}
		}
		return true;
	}
	
}

class ReadWork implements IWork {
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
