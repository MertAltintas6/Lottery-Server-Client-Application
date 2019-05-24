import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

public class GeneratorClient {
	public static void main(String[] args) {
		try {
			boolean valid = false;
			for(int j=0; j<30; j++){
				int port = (int)(Math.random()*2)+5678;
				int clientId = (int)(Math.random()*900)+100;
				Socket socket;
				socket = new Socket(InetAddress.getLocalHost(),port);
				DataInputStream in = new DataInputStream(socket.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				//generate numbers
				int num = 1;
				String[] numbers = new String[6];
				for(int i=0; i <numbers.length;i++) {
					num = (int)(Math.random()*40)+1;
					numbers[i] = Integer.toString(num);
				}
				String msg = Integer.toString(clientId) +": " + Arrays.toString(numbers);
				out.writeObject(msg);
				out.flush();					
				valid = in.readBoolean();
				if(valid)
					System.out.println("OK");
				else
					System.out.println("ERR");
				socket.close();
			}
		}catch(Exception e) {}
		
	}
}