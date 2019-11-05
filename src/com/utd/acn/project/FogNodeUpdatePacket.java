package com.utd.acn.project;

import java.io.Serializable;

public class FogNodeUpdatePacket implements Serializable{
	private static final long serialVersionUID = 1L;
	private FogNodeUpdatePacketHeader header;
	private double currExpectedDelay;
	
	public FogNodeUpdatePacket(FogNodeUpdatePacketHeader header, double currExpectedDelay) {
		this.header = header;
		this.currExpectedDelay = currExpectedDelay;
	}
	
	public FogNodeUpdatePacketHeader getHeader() {
		return header;
	}
	public void setHeader(FogNodeUpdatePacketHeader header) {
		this.header = header;
	}
	public double getCurrExpectedDelay() {
		return currExpectedDelay;
	}
	public void setCurrExpectedDelay(double currExpectedDelay) {
		this.currExpectedDelay = currExpectedDelay;
	}
}
