package com.iocl.integratedunblockingboot.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(SapUnblockCustomersId.class)
public class SapUnblockCustomers {

//	custcode, sales_org, block_id, UNBLOCK_ATTEMPT_NO,DIVISION "

	@Id
	String blockId;
	@Id
	String unblockAttemptNo;
	String unblockStatus;
	int custcode;
	@Column(name="sales_org")
	int salesorg;
	String division;
	LocalDateTime dateUpdated;
	LocalDateTime unblockConfDateTime;
	int blockAttemptNo;
	String mobileNo;
	String smsStatus;
	LocalDateTime smsDateTime;
	String smsText;
	
	
	LocalDateTime blockDate;
	
	String sapUnblockMsg;;
	
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
	public String getUnblockStatus() {
		return unblockStatus;
	}
	public void setUnblockStatus(String unblockStatus) {
		this.unblockStatus = unblockStatus;
	}
	public int getCustcode() {
		return custcode;
	}
	public void setCustcode(int custcode) {
		this.custcode = custcode;
	}
	public int getSalesorg() {
		return salesorg;
	}
	public void setSalesorg(int salesorg) {
		this.salesorg = salesorg;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public LocalDateTime getDateUpdated() {
		return dateUpdated;
	}
	public void setDateUpdated(LocalDateTime dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	public String getSapUnblockMsg() {
		return sapUnblockMsg;
	}
	public void setSapUnblockMsg(String sapUnblockMsg) {
		this.sapUnblockMsg = sapUnblockMsg;
	}
	public LocalDateTime getUnblockConfDateTime() {
		return unblockConfDateTime;
	}
	public void setUnblockConfDateTime(LocalDateTime unblockConfDateTime) {
		this.unblockConfDateTime = unblockConfDateTime;
	}
	public int getBlockAttemptNo() {
		return blockAttemptNo;
	}
	public void setBlockAttemptNo(int blockAttemptNo) {
		this.blockAttemptNo = blockAttemptNo;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getSmsStatus() {
		return smsStatus;
	}
	public void setSmsStatus(String smsStatus) {
		this.smsStatus = smsStatus;
	}
	public LocalDateTime getSmsDateTime() {
		return smsDateTime;
	}
	public void setSmsDateTime(LocalDateTime smsDateTime) {
		this.smsDateTime = smsDateTime;
	}
	public String getSmsText() {
		return smsText;
	}
	public void setSmsText(String smsText) {
		this.smsText = smsText;
	}
	public LocalDateTime getBlockDate() {
		return blockDate;
	}
	public void setBlockDate(LocalDateTime blockDate) {
		this.blockDate = blockDate;
	}
	
	
	
	
}
