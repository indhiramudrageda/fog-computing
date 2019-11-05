package com.utd.acn.project;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class IoTNodeListener extends Thread { 
	private DatagramSocket udpSocket; 
    private IoTNode iotNode;
    
	public IoTNodeListener(IoTNode iotNode) {
		this.iotNode = iotNode;
		try {
			udpSocket =  new DatagramSocket(iotNode.getUdpPort());
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	@Override
    public void run()  
    { 
        while (true)  
        { 
            try { 
                // receive response from fog/cloud node. 
            	byte[] buffer = new byte[1024];
            	DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            	udpSocket.receive(response);
            	byte[] data = response.getData();
            	ByteArrayInputStream baos = new ByteArrayInputStream(data);
                ObjectInputStream oos = new ObjectInputStream(baos);
                Response responseObj = (Response)oos.readObject();
                iotNode.printResponse(responseObj);
            } catch (IOException | ClassNotFoundException e) { 
            	this.udpSocket.close(); 
                e.printStackTrace(); 
            } 
        }           
    } 
} 

