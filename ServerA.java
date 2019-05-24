import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ArrayBlockingQueue;

/**
* Server Configurations
*	ServerA: 
*		clientPort = 5678
*		readingPort = 5000
*		writingPort = 5001
*/

public class ServerA {
	public static void main(String[] args) {
		
		final int clientPort = 5678;
		final int readingPort = 5000;
		final int writingPort = 5001;
		String name = "ServerA";
		String readerName = "ReaderA";
		String writerName = "WriterA";

		LogQueue logQueue = new LogQueue();
		Logger log = new Logger(logQueue);
		log.start();

		ArrayBlockingQueue<String> data = new ArrayBlockingQueue<String>(100);
		Buffer<String> buf = new Buffer<String>(100);
				
		new ClientHandler(name, clientPort, data, buf, logQueue).start();
		new Reader(readerName,readingPort, logQueue, data).start();
		new Writer(writerName, writingPort, buf, logQueue).start();

	}
}
