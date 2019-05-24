import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
* Server Configurations
*	ServerB:
*		clientPort = 5679
*		readingPort = 5001
*		writingPort = 5000
*/

public class ServerB {
	public static void main(String[] args) {
		
		final int clientPort = 5679;
		final int readingPort = 5001;
		final int writingPort = 5000;
		String name = "ServerB";
		String readerName = "ReaderB";
		String writerName = "WriterB";
		

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
