public class Logger extends Thread{
	private  LogQueue logQueue;
	private int size;
	String msg = "";
	public Logger(LogQueue logQueue) {
		this.logQueue = logQueue;
		size = 0;
	}
	
	public void run() {
		System.out.println("Logger is starting");
		while(true) {
			if(size < logQueue.getSize()) {
				msg = logQueue.get(size);	
				System.out.println(msg);
				size++;
			}
		}
	}
}
