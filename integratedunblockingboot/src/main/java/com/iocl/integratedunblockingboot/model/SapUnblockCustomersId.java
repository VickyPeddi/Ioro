package com.iocl.integratedunblockingboot.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class SapUnblockCustomersId implements Serializable{

	
	private static final long serialVersionUID = 4654419915757521704L;
	private String blockId;
	private String unblockAttemptNo;
	public String getBlockId() {
		return blockId;
	}
	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}
	public String getUnblockAttemptNo() {
		return unblockAttemptNo;
	}
	public void setUnblockAttemptNo(String unblockAttemptNo) {
		this.unblockAttemptNo = unblockAttemptNo;
	}
	public SapUnblockCustomersId(String blockId, String unblockAttemptNo) {
		super();
		this.blockId = blockId;
		this.unblockAttemptNo = unblockAttemptNo;
	}
	public SapUnblockCustomersId() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
