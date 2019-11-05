package com.utd.acn.project;

import java.io.Serializable;

public class FogNodeUpdatePacket implements Serializable{
	private static final long serialVersionUID = 1L;
	private FogNodeUpdatePacketHeader header;
	private int currExpectedDelay;
	
	public FogNodeUpdatePacket(FogNodeUpdatePacketHeader header, int currExpectedDelay) {
		this.header = header;
		this.currExpectedDelay = currExpectedDelay;
	}
	
	public FogNodeUpdatePacketHeader getHeader() {
		return header;
	}
	public void setHeader(FogNodeUpdatePacketHeader header) {
		this.header = header;
	}
	public int getCurrExpectedDelay() {
		return currExpectedDelay;
	}
	public void setCurrExpectedDelay(int currExpectedDelay) {
		this.currExpectedDelay = currExpectedDelay;
	}
}
