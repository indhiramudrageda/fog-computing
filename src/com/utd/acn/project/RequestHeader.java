package com.utd.acn.project;

import java.io.Serializable;

public class RequestHeader implements Serializable{
	private static final long serialVersionUID = 1L;
	private String prevNodeIP;
	private int prevNodePort;
	private String sourceIP;
	private int sourcePort;
	private String destinationIP;
	private int destinationPort;
	private String protocol;
	private int sequenceNumber;
	private int forwardingLimit;
	private int processingTime;
	
	public RequestHeader(String prevNodeIP, int prevNodePort, String sourceIP, int sPort, String destinationIP, int dPort, String protocol, int seqNum, int forwardLt, int processingTime) {
		this.prevNodeIP = prevNodeIP;
		this.prevNodePort = prevNodePort;
		this.sourceIP = sourceIP;
		this.sourcePort = sPort;
		this.destinationIP = destinationIP;
		this.destinationPort = dPort;
		this.protocol = protocol;
		this.sequenceNumber = seqNum;
		this.forwardingLimit = forwardLt;
		this.processingTime = processingTime;
	}
	public String getPrevNodeIP() {
		return prevNodeIP;
	}
	public void setPrevNodeIP(String prevNodeIP) {
		this.prevNodeIP = prevNodeIP;
	}
	public int getPrevNodePort() {
		return prevNodePort;
	}
	public void setPrevNodePort(int prevNodePort) {
		this.prevNodePort = prevNodePort;
	}
	public String getSourceIP() {
		return sourceIP;
	}
	public void setSourceIP(String sourceIP) {
		this.sourceIP = sourceIP;
	}
	public int getSourcePort() {
		return sourcePort;
	}
	public void setSourcePort(int sourcePort) {
		this.sourcePort = sourcePort;
	}
	public String getDestinationIP() {
		return destinationIP;
	}
	public void setDestinationIP(String destinationIP) {
		this.destinationIP = destinationIP;
	}
	public int getDestinationPort() {
		return destinationPort;
	}
	public void setDestinationPort(int destinationPort) {
		this.destinationPort = destinationPort;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public int getForwardingLimit() {
		return forwardingLimit;
	}
	public void setForwardingLimit(int forwardingLimit) {
		this.forwardingLimit = forwardingLimit;
	}
	public int getProcessingTime() {
		return processingTime;
	}
	public void setProcessingTime(int processingTime) {
		this.processingTime = processingTime;
	}
}
