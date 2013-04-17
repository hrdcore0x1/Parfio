package testing;

import parfio.ParfioReaderNIO2;

public class TestParfioReaderNIO2 {

	public TestParfioReaderNIO2() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ParfioReaderNIO2 prn = new ParfioReaderNIO2("testnums.txt");
		String input = "";
		while((input = prn.read()) != null){
			System.out.println(input);
		}

	}

}
