package com.utd.acn.project;

import java.io.Serializable;

public class FogNodeUpdatePacketHeader implements Serializable{
	private static final long serialVersionUID = 1L;
	private String sourceIP;
	private int sourcePort;
	private String destinationIP;
	private int destinationPort;
	private String protocol;
	
	public FogNodeUpdatePacketHeader(String sourceIP, int sPort, String destinationIP, int dPort, String protocol) {
		this.sourceIP = sourceIP;
		this.sourcePort = sPort;
		this.destinationIP = destinationIP;
		this.destinationPort = dPort;
		this.protocol = protocol;
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
}
