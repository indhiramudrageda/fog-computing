package com.utd.acn.project;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CloudNode extends Node{
	private Queue<Request> processingQueue = new ConcurrentLinkedQueue<Request>();

	public CloudNode(String ipAddress, int tcpPort, int udpPort) {
		super(ipAddress, tcpPort, udpPort);             
	}
	
	private void intitializeListeners() {
		//Listen to fog nodes for incoming requests and update packets with current processing delay.
		listenToFogNodes();
		
		//Thread handling pending requests in the queue.
		CloudNodeRequestHandler reqHandler = new CloudNodeRequestHandler(this);
		reqHandler.start();
	}
	
	private void listenToFogNodes() {
		CloudNodeListener tcpListener = new CloudNodeListener(this);
		tcpListener.start();
	}
	
	protected void processRequest(Request request) {
		appendAuditInfo(request, "["+request.getHeader().getSequenceNumber()+"]CLOUD NODE:"+ getIpAddress() + ":"+ getTcpPort()+": Request has been received.");	
		getProcessingQueue().add(request);
	}
	
	private Response prepareResponse(Request request) {
		appendAuditInfo(request, "["+request.getHeader().getSequenceNumber()+"]CLOUD NODE:"+ getIpAddress()+ ":"+ getTcpPort()+": Response is being generated and sent to source "+ request.getHeader().getSourceIP()+": "+request.getHeader().getSourcePort());
		ResponseHeader header = new ResponseHeader(request.getHeader().getSourceIP(), request.getHeader().getSourcePort(), 
				request.getHeader().getDestinationIP(), request.getHeader().getDestinationPort(), "UDP", request.getHeader().getSequenceNumber());
		Response response = new Response(header, request.getAuditTrail());
		return response;
	}

	protected void sendResponse(Request request) {
		Response response = prepareResponse(request);
		send(response, response.getHeader().getSourceIP(), response.getHeader().getSourcePort(), "UDP");
	}

	public Queue<Request> getProcessingQueue() {
		return processingQueue;
	}

	public void setProcessingQueue(Queue<Request> processingQueue) {
		this.processingQueue = processingQueue;
	}

	public static void main(String args[]) {
		//java CloudNode MY_TCP
		//java CloudNode 127.0.0.1 5331
		
		String ipAddress;
		int tcpPort;
		if(args.length > 0) {
			ipAddress = args[0];
			tcpPort = Integer.parseInt(args[1]);
			CloudNode c = new CloudNode(ipAddress, tcpPort, 0);
			c.intitializeListeners();
		} else {
			System.out.println("Improper arguments passed!");
		}
	}
}
