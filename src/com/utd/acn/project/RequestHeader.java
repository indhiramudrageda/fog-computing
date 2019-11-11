package com.utd.acn.project;

import java.io.Serializable;

public class RequestHeader implements Serializable{
	private static final long serialVersionUID = 1L;
	private String comingFromIP;
	private int comingFromPort;
	private String sourceIP;
	private int sourcePort;
	private String destinationIP;
	private int destinationPort;
	private String protocol;
	private int sequenceNumber;
	private int forwardingLimit;
	private int processingTime;
	
	public RequestHeader(String comingFromIP, int comingFromPort, String sourceIP, int sPort, String destinationIP, int dPort, String protocol, int seqNum, int forwardLt, int processingTime) {
		this.comingFromIP = comingFromIP;
		this.comingFromPort = comingFromPort;
		this.sourceIP = sourceIP;
		this.sourcePort = sPort;
		this.destinationIP = destinationIP;
		this.destinationPort = dPort;
		this.protocol = protocol;
		this.sequenceNumber = seqNum;
		this.forwardingLimit = forwardLt;
		this.processingTime = processingTime;
	}
	
	public String getComingFromIP() {
		return comingFromIP;
	}

	public void setComingFromIP(String comingFromIP) {
		this.comingFromIP = comingFromIP;
	}

	public int getComingFromPort() {
		return comingFromPort;
	}

	public void setComingFromPort(int comingFromPort) {
		this.comingFromPort = comingFromPort;
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
