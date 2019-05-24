import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.net.InetAddress;


public class Writer extends Thread{
	private  Buffer<String> buffer;
	private  LogQueue logQueue;
	private int writingPort;
	String numbers;
	String name;
	DataInputStream in;
	ObjectOutputStream out;
	boolean con;
	boolean valid;
	int times;
	String msg;
	public Writer(String name, int writingPort, Buffer<String> buffer, LogQueue logQueue) {
		this.writingPort = writingPort;
		this.buffer = buffer;
		this.logQueue = logQueue;
		this.name = name;
		numbers = "";
		con = false;
		valid = false;
		times =0;
		msg ="";
	}
	
	public void run() {
		Socket socket = new Socket();;
		try{
			while(true){
				while(this.buffer.getSize()!=0){
					if(!con){
						socket = new Socket(InetAddress.getLocalHost(),this.writingPort);
						in = new DataInputStream(socket.getInputStream());
						out = new ObjectOutputStream(socket.getOutputStream());
						msg = "CON";
						out.writeObject(msg);
						out.flush();
						con = in.readBoolean();
					}
					if(con && times ==0){
						this.logQueue.add(this.name + ": CONNECTION OK! to the port "+ this.writingPort);
						times = 1;
					}
					try{
						numbers = this.buffer.get();
						msg = "DAT";
						out.writeObject(msg);
					}catch(Exception e){}	
					out.writeObject(numbers);
					out.flush();
					valid = in.readBoolean();
					if(valid){
						this.logQueue.add(this.name + ": OK! Ticket "+ numbers +" has sent to port number: " + this.writingPort);
					}
					else{
						this.logQueue.add(this.name + ": ERR! Ticket "+ numbers +" sending problem to the port number: " + this.writingPort);
					}
				}
				
					//socket.close();
					//con = false;		
					//times = 0;	
				
			}	
		}catch(Exception e){}
	}
}