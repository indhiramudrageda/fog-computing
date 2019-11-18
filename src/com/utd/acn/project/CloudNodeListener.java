package com.utd.acn.project;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CloudNodeListener extends Thread{ 
	private InputStream inStream;
    private ObjectInputStream objectInputStream; 
    private ServerSocket tcpServersocket; 
    private Socket socket;
    private CloudNode cloudNode;
    
    public CloudNodeListener(CloudNode cloudNode) {
    	this.cloudNode = cloudNode;
    	try {
			this.tcpServersocket = new ServerSocket(cloudNode.getTcpPort());
		} catch (IOException e) {
			System.out.println("Error creating server socket: "+ e.getMessage());
		}
	}

	@Override
    public void run()  
    { 
        while (true)  
        { 
            try { 
            	// receive requests from fog nodes. 
            	socket = tcpServersocket.accept();
            	inStream = socket.getInputStream();
        		objectInputStream = new ObjectInputStream(inStream);
                Request request = (Request)objectInputStream.readObject();
                cloudNode.processRequest(request);
            } catch (IOException | ClassNotFoundException e) { 
            	System.out.println("Error reading the request/update packet at fog node:" + cloudNode.getIpAddress()); 
                try {
					inStream.close();
					socket.close();
				} catch (IOException e1) {
					System.out.println("Error closing connections: "+ e.getMessage());
				} 
            } 
        }           
    } 
}
