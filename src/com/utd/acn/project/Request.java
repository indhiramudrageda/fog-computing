package com.utd.acn.project;

import java.io.Serializable;

public class Request implements Serializable{

	private static final long serialVersionUID = 1L;
	private RequestHeader header;
	private String auditTrail;
	
	public Request(RequestHeader header, String audit) {
		this.header = header;
		this.auditTrail = audit;
	}
	public RequestHeader getHeader() {
		return header;
	}
	public void setHeader(RequestHeader header) {
		this.header = header;
	}
	public String getAuditTrail() {
		return auditTrail;
	}
	public void setAuditTrail(String auditTrail) {
		this.auditTrail = auditTrail;
	}
	public void appendAuditTrail(String auditTrail) {
		this.auditTrail = this.auditTrail + "\n" + auditTrail;
	}
	public void decrementForwardingLt() {
		int currForwardingLt = getHeader().getForwardingLimit();
		getHeader().setForwardingLimit(currForwardingLt-1);
	}
}
