package com.utd.acn.project;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CloudNode extends Node{
	private Queue<Request> processingQueue = new ConcurrentLinkedQueue<Request>();

	public CloudNode(String ipAddress, int tcpPort, int udpPort) {
		super(ipAddress, tcpPort, udpPort);             
	}
	
	private void intitializeListeners() {
		//Listen to fog nodes for incoming requests and update packets with current processing delay.
		//serversocket = new ServerSocket(getTcpPort());
		listenToFogNodes();
		
		//Thread handling pending requests in the queue.
		CloudNodeRequestHandler reqHandler = new CloudNodeRequestHandler(this);
		reqHandler.start();
	}
	
	private void listenToFogNodes() {
		CloudNodeListener tcpListener = new CloudNodeListener(this);
		tcpListener.start();
	}
	
	public void processRequest(Request request) {
		appendAuditInfo(request, "["+request.getHeader().getSequenceNumber()+"]CLOUD NODE:"+ getIpAddress() + ":"+ getTcpPort()+": Request has been received.");	
		getProcessingQueue().add(request);
	}
	
	public Response prepareResponse(Request request) {
		appendAuditInfo(request, "["+request.getHeader().getSequenceNumber()+"]CLOUD NODE:"+ getIpAddress()+ ":"+ getTcpPort()+": Response is being generated and sent to source "+ request.getHeader().getSourceIP()+": "+request.getHeader().getSourcePort());
		ResponseHeader header = new ResponseHeader(request.getHeader().getSourceIP(), request.getHeader().getSourcePort(), 
				request.getHeader().getDestinationIP(), request.getHeader().getDestinationPort(), "UDP", request.getHeader().getSequenceNumber());
		Response response = new Response(header, request.getAuditTrail());
		return response;
	}

	public void sendResponse(Request request) {
		Response response = prepareResponse(request);
		send(response, response.getHeader().getDestinationIP(), response.getHeader().getDestinationPort(), "UDP");
//		try{
//			DatagramSocket socket = new DatagramSocket();
//			InetAddress IPAddress = InetAddress.getByName(response.getHeader().getSourceIP());
//			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//			ObjectOutputStream os = new ObjectOutputStream(outputStream);
//			os.writeObject(response);
//			byte[] data = outputStream.toByteArray();
//			DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, response.getHeader().getSourcePort());
//			socket.send(sendPacket);
//			//System.out.println("Response sent to " + response.getHeader().getSourceIP()+": "+response.getHeader().getSourcePort());
//			os.close();
//			socket.close();
//		}catch(Exception e){
//			System.out.println(e);
//		}
	}

	public Queue<Request> getProcessingQueue() {
		return processingQueue;
	}

	public void setProcessingQueue(Queue<Request> processingQueue) {
		this.processingQueue = processingQueue;
	}

	public static void main(String args[]) {
		//java CloudNode MY_TCP
		String ipAddress = "127.0.0.1";
		int port = 5331;
		
		CloudNode c = new CloudNode(ipAddress, port,0);
		c.intitializeListeners();
	}
}
