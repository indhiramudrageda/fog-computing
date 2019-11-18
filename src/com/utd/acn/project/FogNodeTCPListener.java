package com.utd.acn.project;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FogNodeTCPListener extends Thread{ 
	private InputStream inStream;
    private ObjectInputStream objectInputStream; 
    private ServerSocket tcpServersocket; 
    private Socket socket;
    private final FogNode fogNode;
    
    public FogNodeTCPListener(FogNode fogNode) {
    	this.fogNode = fogNode;
    	try {
			tcpServersocket = new ServerSocket(fogNode.getTcpPort());
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
            	// receive updates or requests from neighboring fog nodes.
            	socket = tcpServersocket.accept();
            	inStream = socket.getInputStream();
        		objectInputStream = new ObjectInputStream(inStream); 
                Object obj = objectInputStream.readObject();
                if(obj instanceof FogNodeUpdatePacket) {
					FogNodeUpdatePacket updatePacket = (FogNodeUpdatePacket) obj;
					fogNode.storeNeighborsInfo(updatePacket);
				} else if(obj instanceof Request) {
					Request request = (Request) obj;
					fogNode.processRequest(request);
				}
            } catch (IOException | ClassNotFoundException e) { 
            	System.out.println("Error reading the request/update packet at fog node:" + fogNode.getIpAddress()); 
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
