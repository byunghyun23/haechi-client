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
    			System.out.println("업데이트 요청 대기 중..");
    			Socket socket = serversock.accept();
    			socket.setSoTimeout(300000);
    			System.out.println("수신");
    			Listener listener = new Listener(socket, number);
    			number++;
    			listener.start();
    		}
    		
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }
}