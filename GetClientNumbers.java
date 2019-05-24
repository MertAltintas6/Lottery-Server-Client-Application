import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;

public class GetClientNumbers implements Runnable{
	Socket soc;
	LogQueue logQueue;
	Semaphore sem;
	private ArrayBlockingQueue<String> data;
	Buffer<String> buf;
	int size;
	String name;
	GetClientNumbers(Socket soc, String name, LogQueue logQueue, Semaphore sem, ArrayBlockingQueue<String> data,Buffer<String> buf){
		this.soc= soc;
		this.name = name;
		this.logQueue = logQueue;
		this.sem = sem;
		this.data = data;
		this.buf = buf;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ObjectInputStream in = new ObjectInputStream(this.soc.getInputStream());
			DataOutputStream out = new DataOutputStream(this.soc.getOutputStream());
			String val = (String) in.readObject();
			if(control(val)) {
				this.logQueue.add(this.name + ": OK! Ticket " + val);
				this.data.put(val);
				this.buf.put(val);
				out.writeBoolean(true);
			}
			else{
				this.logQueue.add(this.name + ": ERR! Ticket " + val);
				out.writeBoolean(false);
			}
			this.soc.close();
			this.sem.release();
		}catch(Exception e) {}
	}
	private boolean control(String val) {
		String[] parts = val.split(":");
		if(parts.length !=2 )
			return false;
		
		return true;
	}
}
