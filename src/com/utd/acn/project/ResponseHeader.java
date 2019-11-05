package com.utd.acn.project;

import java.io.Serializable;

public class ResponseHeader implements Serializable{
	private static final long serialVersionUID = 1L;
	private String sourceIP;
	private int sourcePort;
	private String destinationIP;
	private int destinationPort;
	private String protocol;
	private int sequenceNumber;
	
	public ResponseHeader(String sourceIP, int sPort, String destinationIP, int dPort, String protocol, int seqNum) {
		this.sourceIP = sourceIP;
		this.sourcePort = sPort;
		this.destinationIP = destinationIP;
		this.destinationPort = dPort;
		this.protocol = protocol;
		this.sequenceNumber = seqNum;
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
}
