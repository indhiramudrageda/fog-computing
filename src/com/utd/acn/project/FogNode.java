package com.utd.acn.project;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FogNode extends Node {

	private double maxResponseTime;
	private int interval;
	private CloudNode cloudNode;
	private Queue<Request> processingQueue = new ConcurrentLinkedQueue<Request>();
	private ArrayList<FogNode> neighborFogNodes = new ArrayList<FogNode>();
	private double currExpectedDelay;
	
	private FogNode(String ipAddress, int tcpPort, int udpPort, int interval, double maxResponseTime, CloudNode cloudNode, ArrayList<FogNode> fogNodeList) {
		super(ipAddress, tcpPort, udpPort);
		this.interval = interval;
		this.maxResponseTime = maxResponseTime;
		this.cloudNode = cloudNode;
		this.neighborFogNodes = fogNodeList;
		
		//Listen to IoT nodes for incoming requests.
		listenToIoTNodes();

		//Listen to fog nodes for incoming requests and update packets with current processing delay.
		listenToNeighbors();
		
		//Thread handling pending requests in the queue.
		FogNodeRequestHandler reqHandler = new FogNodeRequestHandler(this);
		reqHandler.start();
		
		//scheduler to keep sending update packets to neighboring fog nodes periodically.
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(); 
		scheduler.scheduleAtFixedRate(new UpdateNeighborsScheduler(this), 5, interval, TimeUnit.SECONDS);
	}
	
	public FogNode(String ipAddress, int tcpPort, int udpPort) {
		super(ipAddress, tcpPort, udpPort);
	}
	
	private void listenToIoTNodes() {
		FogNodeUDPListener udpListener = new FogNodeUDPListener(this);
		udpListener.start();
	}
	
	private void listenToNeighbors() {
		FogNodeTCPListener tcpListener = new FogNodeTCPListener(this);
		tcpListener.start();
	}

	public void processRequest(Request request) {
		appendAuditInfo(request, "["+request.getHeader().getSequenceNumber()+"]FOG NODE:"+ getIpAddress() + ":"+getUdpPort()+": Request has been received.");
		int currForwardingLt = request.getHeader().getForwardingLimit();
		request.getHeader().setForwardingLimit(currForwardingLt-1);
		double expectedDelay = request.getHeader().getProcessingTime();
		Iterator<Request> itr = getProcessingQueue().iterator();
		while (itr.hasNext()) 
		{
			expectedDelay += itr.next().getHeader().getProcessingTime();
		}
		
		if(maxResponseTime >= expectedDelay) {
			//add request to queue
			getProcessingQueue().add(request);
		} else {
			if(request.getHeader().getForwardingLimit() == 0)
				forwardRequestToCloud(request);
			else
				forwardRequest(request);
		}
	}
	
	private void forwardRequest(Request request) {
		FogNode bestNode = getBestNeighbor(request);
		if(bestNode == null)
			forwardRequestToCloud(request);
		else {
			appendAuditInfo(request, "["+request.getHeader().getSequenceNumber()+"]FOG NODE:"+ getIpAddress() + ":"+ getTcpPort()+": Forwarding request to neighboring fog node :"+ bestNode.getIpAddress()+":"+bestNode.getTcpPort());
			send(request, bestNode.getIpAddress(), bestNode.getTcpPort(), "TCP");
//			try{
//				Socket s = new Socket(bestNode.getIpAddress(), bestNode.getTcpPort());
//				OutputStream os = s.getOutputStream();
//				ObjectOutputStream oos = new ObjectOutputStream(os);
//				oos.writeObject(request);
//				//oos.writeObject(new String("forwarding to neighboring fog node: "+ bestNode.getIpAddress()));
//				oos.close();
//				os.close();
//				s.close();
//			}catch(Exception e){
//				System.out.println(e);
//				System.out.println("Error connecting to fog node. Hence forwarding request to cloud.");
//				forwardRequestToCloud(request);
//			}
		}
		
	}
	
	private void forwardRequestToCloud(Request request) {
		appendAuditInfo(request, "["+request.getHeader().getSequenceNumber()+"]FOG NODE:"+  getIpAddress() + ":"+ getTcpPort()+": Forwarding request to cloud node "+  getCloudNode().getIpAddress()+":"+ getCloudNode().getTcpPort());
		send(request, getCloudNode().getIpAddress(),getCloudNode().getTcpPort(), "TCP");
//		try{
//			Socket s = new Socket(getCloudNode().getIpAddress(),getCloudNode().getTcpPort());
//			OutputStream os = s.getOutputStream();
//			ObjectOutputStream oos = new ObjectOutputStream(os);
//			oos.writeObject(request);
//			//oos.writeObject(new String("another object from the client"));
//			oos.close();
//			os.close();
//			s.close();
//		}catch(Exception e){
//			System.out.println(e);
//		}
	}
	
	public void updateNeighbors() {
		double currExpectedDelay = 0;
		for(Request req : getProcessingQueue()) {
			currExpectedDelay += req.getHeader().getProcessingTime();
		}
		for(FogNode neighbor : getNeighborFogNodes()) {
			FogNodeUpdatePacketHeader header = new FogNodeUpdatePacketHeader(getIpAddress(), getTcpPort(), 
					neighbor.getIpAddress(), neighbor.getTcpPort(), "TCP");
			FogNodeUpdatePacket updatePacket = new FogNodeUpdatePacket(header, currExpectedDelay);
			send(updatePacket, header.getDestinationIP(), header.getDestinationPort(), "TCP");
//			try {
//				Socket s = new Socket(header.getDestinationIP(), header.getDestinationPort());
//				OutputStream os = s.getOutputStream();
//				ObjectOutputStream oos = new ObjectOutputStream(os);
//				oos.writeObject(updatePacket);
//				//oos.writeObject(new String("sending fog node update packet to :"+header.getDestinationIP()));
//				oos.close();
//				os.close();
//				s.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
	}
	
	public void storeNeighborsInfo(FogNodeUpdatePacket updatePacket) {
		for(FogNode neighbor : getNeighborFogNodes()) { 
			if(neighbor.getIpAddress().equals(updatePacket.getHeader().getSourceIP())) {
				neighbor.setCurrExpectedDelay(updatePacket.getCurrExpectedDelay());
				return;
			}
		}
	}
	
	private FogNode getBestNeighbor(Request request) {
		FogNode minDelayNeighbor = null;
		for(FogNode neighbor : getNeighborFogNodes()) {
			if(!request.getAuditTrail().contains(neighbor.getTcpPort()+"")) {
				if(minDelayNeighbor == null)
					minDelayNeighbor = neighbor;
				else if(minDelayNeighbor.getCurrExpectedDelay() > neighbor.getCurrExpectedDelay())
						minDelayNeighbor = neighbor;
			}
		}
		return minDelayNeighbor;
	}
	
	private Response prepareResponse(Request request) {
		appendAuditInfo(request, "["+request.getHeader().getSequenceNumber()+"]FOG NODE:"+ getIpAddress()+ ":"+getUdpPort()+": Response is being generated and sent to source "+ request.getHeader().getSourceIP()+": "+request.getHeader().getSourcePort());
		ResponseHeader header = new ResponseHeader(request.getHeader().getSourceIP(), request.getHeader().getSourcePort(), 
				request.getHeader().getDestinationIP(), request.getHeader().getDestinationPort(), "UDP", request.getHeader().getSequenceNumber());
		Response response = new Response(header, request.getAuditTrail());
		return response;
	}
	
	public void sendResponse(Request request) {
		Response response = prepareResponse(request);
		send(response, response.getHeader().getSourceIP(), response.getHeader().getSourcePort(), "UDP");
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
	
	public static void main(String args[]) {
		//java Max_Response_Time t MY_TCP MY_UDP C TCP0 N1 TCP1 N2 TCP2
		//String TCP1 = "4 3 5325 9876 127.0.0.1 5331 127.0.0.1 5326 127.0.0.1 5328";
		//String TCP2 = "4 3 5326 9877 127.0.0.1 5331 127.0.0.1 5325 127.0.0.1 5327 127.0.0.1 5328";
		//String TCP3 = "4 3 5327 9878 127.0.0.1 5331 127.0.0.1 5326 127.0.0.1 5329 127.0.0.1 5330";
		//String TCP4 = "4 3 5328 9879 127.0.0.1 5331 127.0.0.1 5325 127.0.0.1 5326 127.0.0.1 5329";
		//String TCP5 = "4 3 5329 9880 127.0.0.1 5331 127.0.0.1 5327 127.0.0.1 5328 127.0.0.1 5330";
		//String TCP6 = "4 3 5330 9881 127.0.0.1 5331 127.0.0.1 5327 127.0.0.1 5329";
		
		//args = cmd.split(" ");
		  double maxResponseTime; 
		  int interval; 
		  int tcpPort;
		  int udpPort; 
		  if(args.length > 0) 
		  { 
			  maxResponseTime = Double.parseDouble(args[0]); 
			  interval = Integer.parseInt(args[1]); 
			  tcpPort = Integer.parseInt(args[2]); 
			  udpPort = Integer.parseInt(args[3]); 
			  CloudNode c = new CloudNode(args[4], Integer.parseInt(args[5]), 0);
			  ArrayList<FogNode> tempFogNodeList = new ArrayList<FogNode>(); 
			  for(int i=6; i<args.length;i++) { 
				  FogNode n = new FogNode(args[i], Integer.parseInt(args[++i]), 0); 
				  tempFogNodeList.add(n); 
			  } 
			  new FogNode("127.0.0.1", tcpPort, udpPort, interval, maxResponseTime, c, tempFogNodeList); 
		  } else {
			  System.out.println("Improper arguments passed!"); 
		  }
	}
	
	public double getMaxResponseTime() {
		return maxResponseTime;
	}

	public void setMaxResponseTime(double maxResponseTime) {
		this.maxResponseTime = maxResponseTime;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public CloudNode getCloudNode() {
		return cloudNode;
	}

	public void setCloudNode(CloudNode cloudNode) {
		this.cloudNode = cloudNode;
	}

	public Queue<Request> getProcessingQueue() {
		return processingQueue;
	}

	public void setProcessingQueue(Queue<Request> processingQueue) {
		this.processingQueue = processingQueue;
	}
	
	public void addToProcessingQueue(Request req) {
		this.processingQueue.add(req);
	}

	public ArrayList<FogNode> getNeighborFogNodes() {
		return neighborFogNodes;
	}

	public void setNeighborFogNodes(ArrayList<FogNode> neighborFogNodes) {
		this.neighborFogNodes = neighborFogNodes;
	}
	
	public void addNeighborFogNode(FogNode neighborFogNode) {
		this.neighborFogNodes.add(neighborFogNode);
	}

	public double getCurrExpectedDelay() {
		return currExpectedDelay;
	}

	public void setCurrExpectedDelay(double currExpectedDelay) {
		this.currExpectedDelay = currExpectedDelay;
	}
}
