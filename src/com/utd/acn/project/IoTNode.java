package com.utd.acn.project;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IoTNode extends Node{
	
	private int interval;
	private ArrayList<FogNode> neighborFogNodes = new ArrayList<FogNode>();
	private int sequenceNum; 
	
	private IoTNode(String ipAddress, int tcpPort, int udpPort, int interval, ArrayList<FogNode> fogNodeList) {
		super(ipAddress, tcpPort, udpPort);
		this.interval = interval;
		this.neighborFogNodes = fogNodeList;
		this.sequenceNum = getRandomIntegerBetweenRange(0,Integer.MAX_VALUE);
		
		//start listener to listen on any responses from fog/cloud nodes.
		listenForResponse();
		
		//scheduler to keep generating requests periodically.
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(); 
		scheduler.scheduleAtFixedRate(new RequestsScheduler(this), 5, interval, TimeUnit.SECONDS);
	}

	public IoTNode(String ipAddress, int tcpPort, int udpPort) {
		super(ipAddress, tcpPort, udpPort);
	}
	
	private Request prepareRequest() {
		int randomIndex = getRandomIntegerBetweenRange(0, neighborFogNodes.size()-1);
		FogNode destinationNode = getNeighborFogNodes().get(randomIndex);
		RequestHeader header = new RequestHeader(getIpAddress(), getUdpPort(),getIpAddress(), getUdpPort(), destinationNode.getIpAddress(), destinationNode.getUdpPort(), 
				"UDP", getNextSequenceNum(), getRandomIntegerBetweenRange(2,5), getRandomIntegerBetweenRange(3,7));
		Request request = new Request(header, "["+header.getSequenceNumber()+"]IoT NODE:"+ getIpAddress()+ ":"+ getUdpPort()+": Request has been generated and being sent to "+destinationNode.getIpAddress()+": "+destinationNode.getUdpPort());
		return request;
	}
	
	protected void sendRequest() {
		Request request = prepareRequest();
		send(request, request.getHeader().getDestinationIP(), request.getHeader().getDestinationPort(), "UDP");
	}
	
	private void listenForResponse() {
		IoTNodeListener listener = new IoTNodeListener(this);
		listener.start();
	}

	protected void printResponse(Response response) {
		response.appendAuditTrail("["+response.getHeader().getSequenceNumber()+"]IoT NODE:"+ getIpAddress()+ ":"+ getUdpPort()+": Response has been received.");
		System.out.println(response.getAuditTrail());
		System.out.println();
	}
	
	public static void main(String args[]) {
		//java IoTNode interval MY_UDP IP1 UDP1 IP2 UDP2
		//String cmd = "3 9882 127.0.0.1 9876 127.0.0.1 9879";
		//args = cmd.split(" ");
		
		int interval;
		int udpPort;
			try {
				interval = Integer.parseInt(args[0]);
				udpPort = Integer.parseInt(args[1]);
				ArrayList<FogNode> tempFogNodeList = new ArrayList<FogNode>();
				for (int i = 2; i < args.length; i++) {
					FogNode n = new FogNode(args[i], 0, Integer.parseInt(args[++i]));
					tempFogNodeList.add(n);
				}
				InetAddress inetAddress = InetAddress.getLocalHost();
				new IoTNode(inetAddress.getHostAddress(), 0, udpPort, interval, tempFogNodeList);
			} catch (UnknownHostException uhe) {
				  System.out.println("Host unknown");
			} catch (Exception e) {
				System.out.println("Improper arguments passed! Expected input format: interval MY_UDP IP1 UDP1 IP2 UDP2");
			}
	}
	
	public static int getRandomIntegerBetweenRange(double min, double max){
	    double x = (int)(Math.random()*((max-min)+1))+min;
	    return (int) x;
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

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getSequenceNum() {
		return sequenceNum;
	}
	
	public int getNextSequenceNum() {
		if(sequenceNum == Integer.MAX_VALUE)
			sequenceNum = 0;
		else 
			sequenceNum++;
		return sequenceNum;
	}
	public void setSequenceNum(int sequenceNum) {
		this.sequenceNum = sequenceNum;
	}
}
