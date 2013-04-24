package testing;

import java.io.FileNotFoundException;
import java.io.IOException;

import parfio.*;

public class TrafficTester {

	public TrafficTester() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		ParfioTraffic.loadSettings("testConfig.Properties");

	}

}
