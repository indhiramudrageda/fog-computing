package com.utd.acn.project;

import java.util.Iterator;
import java.util.Queue;

public class CloudNodeRequestHandler  extends Thread{

	private CloudNode cloudNode;
	
	public CloudNodeRequestHandler(CloudNode cloudNode) {
		this.cloudNode = cloudNode;
	}
	public void run() 
    { 
        try
        { 
           while(true) {
        	  Queue<Request> processingQueue = cloudNode.getProcessingQueue();
        	  Iterator<Request> itr = processingQueue.iterator();
        	  while(itr.hasNext()) {
        		  Request request = (Request) itr.next();
        		  //sleep
        		  Thread.sleep((long) request.getHeader().getProcessingTime()*1000);
      			  //remove from queue
        		  cloudNode.getProcessingQueue().remove(request);
      			  //append audit
        		  cloudNode.appendAuditInfo(request, "["+request.getHeader().getSequenceNumber()+"]CLOUD NODE:"+ cloudNode.getIpAddress()+ ":"+cloudNode.getTcpPort()+": Processing completed.");
      			  //send response
      			  cloudNode.sendResponse(request);
        	  }
           }
        } 
        catch (Exception e) 
        { 
        	System.out.println("Error processing the request at cloud node:" + cloudNode.getIpAddress()); 
        } 
    } 
}