import java.util.ArrayList;
import java.util.concurrent.locks.*;
import java.util.Date;

public class LogQueue {
	private ArrayList<String> logQueue;
	private Lock lock;
	
	public LogQueue(){
		logQueue = new ArrayList<String>();
		lock = new ReentrantLock();
	}
	
	public void add(String m) {
		try {
			lock.lock();
			Date date = new Date();
			String str = String.format("%tc", date );
			String msg = str +"- "+m;
			logQueue.add(msg);
			lock.unlock();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public String get(int index) {
		String msg = "";
		msg = logQueue.get(index);	
		return msg;

	}
	public int getSize() {
		lock.lock();
		int size = logQueue.size();
		lock.unlock();
		return size;
	}

}
