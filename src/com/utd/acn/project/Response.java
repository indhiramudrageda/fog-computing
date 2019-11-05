package com.utd.acn.project;

import java.io.Serializable;

public class Response implements Serializable{
	private static final long serialVersionUID = 1L;
	private ResponseHeader header;
	private String auditTrail;
	
	public Response(ResponseHeader header, String audit) {
		this.header = header;
		this.auditTrail = audit;
	}
	
	public ResponseHeader getHeader() {
		return header;
	}
	public void setHeader(ResponseHeader header) {
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
}
