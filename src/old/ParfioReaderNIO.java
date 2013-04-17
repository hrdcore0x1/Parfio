package old;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import omp.lc_omp;

public class ParfioReaderNIO {

	private BufferedReader br;
	private volatile PipedReader pr;
	private volatile PipedWriter pw;
	private ArrayList<Path> paths;
	private Path path;
	private int nThreads;

	public enum Open {
		GUIDED, SEQUENTIAL
	}

	
	public void write(String s){
		try {
			pw.write(s);
			pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	public synchronized String readLine(){
		try {
			return br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	public BufferedReader getBufferedReader() {
		return br;
	}

	public ParfioReaderNIO(ArrayList<File> in_file) throws IOException {
		paths = new ArrayList<Path>();
		pr = new PipedReader();
		pw = new PipedWriter();
		pr.connect(pw);
		br = new BufferedReader(pr);
		
		for(File f : in_file){
			paths.add(Paths.get(f.toString()));
		}
		//sequentialOpen();
		guidedOpen();
	}

	
	
	private void sequentialOpen() {
		Thread t = new Thread() {
			public void run() {
				try {
					for (Path p : paths) {
						BufferedReader br = new BufferedReader(new FileReader(
								p.toString()));
						String input;
						while ((input = br.readLine()) != null) {
							pw.write(input + "\n");
						}
						br.close();
					}
					pw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t.start();
	}

	/* Guided partitioning */
	private void guidedOpen() {
		Thread t = new Thread() {
			public void run() {

				int i=0;
				for (Path p : paths) {
					final Path pp = p;
					Thread tt = new Thread(){
						public void run(){
							
							try {
								System.out.println("New thread!");
								FileChannel fc = FileChannel.open(pp, java.nio.file.StandardOpenOption.READ);
								int size = (int) fc.size();
								int part = size / lc_omp.omp_get_num_threads(); 
								int startChunk = part * lc_omp.omp_get_thread_num();
								int endChunk = startChunk + part;

								ByteBuffer bb = ByteBuffer.allocate(endChunk - startChunk);
								fc.read(bb, startChunk);
								String s = new String(bb.array());
								if (startChunk != 0) s = s.substring(s.indexOf('\n') + 1);
								boolean nextEndLine = false;
								boolean last = false;
								while (!nextEndLine){
									//System.out.println("Searching endline");
									startChunk = endChunk;
									endChunk += part;
									if (endChunk >= size){
										if (last) break;
										last = true;
									}
									if (endChunk > size) endChunk = size;
									bb = ByteBuffer.allocate(endChunk - startChunk);

									fc.read(bb, startChunk);
									String tmp = new String(bb.array());
									for(char c : tmp.toCharArray()){
										if (nextEndLine) break;
										if (c == '\n') {
											nextEndLine = true;
										}
										s += c;
									}
								}
								//System.out.println("Writing: " + s);
								//ParfioNIO.stdin.write(s);

								
							} catch (IOException ex) {
								// TODO Auto-generated catch block
								ex.printStackTrace();
							}
						}
					};
					tt.start();
					/*
					ReadWork rw = new ReadWork(p, pw);
					lc_omp.work(rw, ++i);
					*/
				}
				/*
				try {
					pw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}*/
			}
		};
		t.start();
	}

	/* Guided partitioning *//*
	private void guidedOpen2() {
		ArrayList<GuidedThread> ts = new ArrayList<GuidedThread>();
		int i = 0;
		for(Path p : paths){
			GuidedThread gt = new GuidedThread(new ReadWork(p, pw), ++i);
			gt.start();
			System.out.println("Starting thread " + i);
			ts.add(gt);
		}
		
		for(GuidedThread gt : ts){
			try {
				System.out.println("Waiting for thread " + gt);
				gt.join();
			} catch (InterruptedException ignore) {
			}
		}
		
		try {
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}*/
	
}
