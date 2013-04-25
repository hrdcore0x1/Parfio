package testing;

import java.io.FileNotFoundException;
import java.io.IOException;

import parfio.*;
import omp.lc_omp;
import omp.lc_omp.*;

public class TestPipe {

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		final long N = 10; // 1000000L;
		final long expected = (N * (N + 1) / 2);
		Parfio.loadProperties("pipe.Properties");

		System.out.println("Writing to pipe...");
		lc_omp.work(new PipeWrite(), 0, (int)N, 2);
		System.out.println("Reading from pipe...");
		lc_omp.work(new PipeRead(), 1);
		
		boolean pass = (PipeRead.sum == expected);
		System.out.println("Pass: " + pass);
		lc_omp.finish();
	}

}

class PipeWrite implements IWork {

	@Override
	public boolean evaluate(Work w) {
		try {
			for (int i=w.start; i<=w.end; i += w.step){
				Parfio.write(i + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}

class PipeRead implements IWork {

	public static long sum = 0;
	
	@Override
	public boolean evaluate(Work w) {
		int mySum = 0;
		try {
			for(;Parfio.ready();){
			String line = Parfio.readLine();
			if (line == null) break;
			mySum += Integer.parseInt(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sum += mySum;
		return true;
	}

}