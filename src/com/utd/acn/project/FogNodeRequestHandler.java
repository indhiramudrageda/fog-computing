package com.utd.acn.project;

import java.util.Iterator;
import java.util.Queue;

public class FogNodeRequestHandler extends Thread{

	private FogNode fogNode;
	
	public FogNodeRequestHandler(FogNode fogNode) {
		this.fogNode = fogNode;
	}
	public void run() 
    { 
        try
        { 
           while(true) {
        	  Queue<Request> processingQueue = fogNode.getProcessingQueue();
        	  Iterator<Request> itr = processingQueue.iterator();
        	  while(itr.hasNext()) {
        		  Request request = (Request) itr.next();
        		  //sleep
        		  Thread.sleep((long) request.getHeader().getProcessingTime()*1000);
      			  //remove from queue
        		  fogNode.getProcessingQueue().remove(request);
      			  //append audit
        		  Node.appendAuditInfo(request, "["+request.getHeader().getSequenceNumber()+"]FOG NODE:"+ fogNode.getIpAddress()+ ": Processing completed.");
      			  //send response
      			  fogNode.sendResponse(request);
        	  }
           }
        } 
        catch (Exception e) 
        { 
        	System.out.println("Error processing the request at fog node:" + fogNode.getIpAddress());
        } 
    } 
}
