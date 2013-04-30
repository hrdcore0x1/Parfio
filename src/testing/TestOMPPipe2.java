package testing;

import java.io.FileNotFoundException;
import java.io.IOException;

import parfio.*;
import omp.lc_omp;
import omp.lc_omp.*;

public class TestOMPPipe2 {

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		
		/*
		 * Pipe example
		 * Piping data from A to B and then B to C
		 * A -> B -> C
		 */
		final long N = 10; // 1000000L; // 1000000L;
		final long expected = (N * (N + 1) / 2) * 2;
		Parfio.loadProperties("pipe2.Properties");
		long start = System.currentTimeMillis();
		lc_omp.work(new PipeWrite2(), 0, (int) N, 2);
		lc_omp.work(new PipeRead2(), 1);
		lc_omp.work(new PipeRead2(), 3);
		long end = System.currentTimeMillis();
		boolean pass = (PipeRead2.sum == expected);
		System.out.println("OMP Pipe: Pass = " + pass);
		System.out.println("Time: " + (end - start) + " ms");
		if (!pass)
			System.out.println("Sum: " + PipeRead2.sum + ", Expected: "
					+ expected);
		Parfio.close();
		lc_omp.finish();
	}

}

class PipeWrite2 implements IWork {

	@Override
	public boolean evaluate(Work w) {
		try {
			for (int i = w.start; i <= w.end; i += 1) {
				Parfio.write(i + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}

class PipeRead2 implements IWork {

	public static long sum = 0;

	@Override
	public boolean evaluate(Work w) {
		long mySum = 0, i=0;
		try {
			
			for (; Parfio.ready();) {
				String line = Parfio.readLine();

				if (line == null)
					break;
			
				if (lc_omp.omp_get_taskid() == 1) {
					Parfio.write(line + "\n");
					//if (++i%20 == 0) System.out.println(line);
				}else{
					//if (++i%20 == 0) System.out.println(line);
				}
				
				
				mySum += Integer.parseInt(line);
			}
		} catch (IOException ignore) {
		}
		sum += mySum;
		return true;
	}

}