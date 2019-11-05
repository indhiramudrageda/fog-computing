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
			e.printStackTrace();
		}
	}

	@Override
    public void run()  
    { 
        while (true)  
        { 
            try { 
            	// receive updates from neighboring fog nodes. 
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
            	// closing resources TO-DO: Finally
                try {
					inStream.close();
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				} 
                e.printStackTrace(); 
            } 
        }           
    } 
}
