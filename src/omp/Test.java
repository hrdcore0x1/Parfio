package omp;

import omp.lc_omp.Work;

public class Test implements lc_omp.IWork {

	int start;
	int end;
	int number_of_primes = 1;
	int number_of_41primes = 0;
	int number_of_43primes = 0;
	int print_primes = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Test t = new Test();

		t.start = 3;
		t.end = 1024;
		System.out.println("Range to check for Primes: " + t.start + " - "
				+ t.end);
		double time = lc_omp.omp_get_wtime();
		lc_omp.work(t, t.start, t.end, 2, 1, 0, lc_omp.DYNAMIC, 100);

		System.out.printf("time=%gs\n", lc_omp.omp_get_wtime() - time);
		System.out.printf("%d primes \n", t.number_of_primes);
		System.out.printf("4n+1 primes: %d\n", t.number_of_41primes);
		System.out.printf("4n-1 primes: %d\n", t.number_of_43primes);
		
		lc_omp.finish();
		

	}

	@Override
	public boolean evaluate(Work w) {
		int i;
		int j;
		int limit;
		int prime;

		int number_of_primesX = 0;
		int number_of_41primesX = 1;
		int number_of_43primesX = 0;

		
		System.out.println("Thread " + lc_omp.omp_get_thread_num() + ": " + w.start + " - " + w.end);
		for (i = w.start; i <= w.end; i += 2) {
			limit = (int) Math.sqrt((float) i) + 1;

			prime = 1;
			j = 3;
			while (prime != 0 && (j <= limit)) {
				if (i % j == 0)
					prime = 0;
				j += 2;
			}

			if (prime != 0) {
				if (print_primes != 0)
					System.out.printf("%d ", i);
				number_of_primesX++;
				if (i % 4 == 1)
					number_of_41primesX++;
				if (i % 4 == 3)
					number_of_43primesX++;
			}
		}
		synchronized (lc_omp.critical()) {
			number_of_primes += number_of_primesX;
			number_of_41primes += number_of_41primesX;
			number_of_43primes += number_of_43primesX;
		}
		return true;
	}

}
