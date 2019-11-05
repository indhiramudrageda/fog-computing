package com.utd.acn.project;

public class RequestsScheduler implements Runnable {

	private IoTNode ioTNode;
	public RequestsScheduler(IoTNode ioTNode) {
		this.ioTNode = ioTNode;
	}

	@Override
	public void run() {
		ioTNode.sendRequest();
	}

}
