package com.utd.acn.project;

public class UpdateNeighborsScheduler implements Runnable {

	private FogNode fogNode;
	
	public UpdateNeighborsScheduler(FogNode fogNode) {
		this.fogNode = fogNode;
	}

	@Override public void run() { 
		fogNode.updateNeighbors();
	}
}
