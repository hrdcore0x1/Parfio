package testing;

import java.io.FileNotFoundException;
import java.io.IOException;

import omp.lc_omp;
import omp.lc_omp.IWork;
import omp.lc_omp.Work;
import parfio.Parfio;

public class TrafficTester {

	public static void main(String[] args) throws FileNotFoundException, IOException {
        
		Parfio.loadProperties("testConfig.Properties");
		System.out.println("Thread group 1");
		lc_omp.work(new Test1(), 1);
		System.out.println("\nThread group 2");
		lc_omp.work(new Test1(), 2);
		lc_omp.finish();

	}

}

class Test1 implements IWork{

	@Override
	public boolean evaluate(Work w) {
		try {
			for(;;){
			String line = Parfio.readLine();
			if (line == null) break;
			System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return true;
		
	}
	
}
