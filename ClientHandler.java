import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class ClientHandler extends Thread{
	int port;
	private ArrayBlockingQueue<String> data;
	Buffer<String> buf;
	LogQueue logQueue;
	final int nThreads =Runtime.getRuntime().availableProcessors();
	ExecutorService pool = Executors.newFixedThreadPool(nThreads);
	Semaphore sem = new Semaphore(100);
	String name;
	public ClientHandler(String name,int port,ArrayBlockingQueue<String> data ,Buffer<String> buf,LogQueue logQueue){
		this.name = name;
		this.port = port;
		this.data = data;
		this.buf = buf;
		this.logQueue = logQueue;
	}

	public void run(){
		ServerSocket clientSocket;
		try {
			clientSocket = new ServerSocket(this.port);
			logQueue.add(this.name + " is running on port " + this.port);
			while(true) {
				try {
					sem.acquire();
				}catch(Exception e) {}
				Socket cliSoc= clientSocket.accept();	
				pool.submit(new GetClientNumbers(cliSoc, this.name,this.logQueue, sem, this.data, this.buf));
				try{Thread.sleep(100);}catch(Exception e){}
			}
		}catch(Exception e) {}
	}
} 