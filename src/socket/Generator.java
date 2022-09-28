package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Generator extends Thread {
	int number = 1;
    @Override
    public void run() {
    	
    	try {
    		ServerSocket serversock = new ServerSocket(5757);
    		
    		while (true) {
    			System.out.println("Pending update request..");
    			Socket socket = serversock.accept();
    			socket.setSoTimeout(300000);
    			System.out.println("Reception!");
    			Listener listener = new Listener(socket, number);
    			number++;
    			listener.start();
    		}
    		
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }
}