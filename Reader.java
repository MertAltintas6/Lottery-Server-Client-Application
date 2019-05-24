import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

class Reader extends Thread implements java.io.Serializable{
	int readingPort;
	LogQueue logQueue;
	private ArrayBlockingQueue<String> data;
	String name;
	Reader(String name,int readingPort, LogQueue logQueue, ArrayBlockingQueue<String> data){
		this.readingPort= readingPort;
		this.logQueue = logQueue;
		this.data = data;
		this.name = name;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ServerSocket replicationSocket;
			replicationSocket = new ServerSocket(this.readingPort);
//			logQueue.add(this.name + " is running on readingPort " + this.readingPort);
			String val = "";
			Socket readerSocket = replicationSocket.accept();	
			ObjectInputStream in = new ObjectInputStream(readerSocket.getInputStream());	
			DataOutputStream out = new DataOutputStream(readerSocket.getOutputStream());
			while(true){			
				val = (String) (in.readObject());
				if(val.equals("CON")){
					out.writeBoolean(true);
				}
				else if(val.equals("DAT")){
					String dat = (String) (in.readObject());
					if(!this.data.contains(dat)) {
						this.data.add(dat);
						this.logQueue.add(this.name + ": Ticket " + dat + " got from readingPort number: " + this.readingPort);
						out.writeBoolean(true);
					}
					else {
						this.logQueue.add(this.name+": Error in Replication! readingPort number: " + this.readingPort);
						out.writeBoolean(false);
					}
					
				}
			}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}