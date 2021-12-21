package com.iocl.integratedunblockingboot.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkUnblockRequestPayLoad {

	@JsonProperty("IT_CUSTOMER")
	private ItCustomer itCustomer;

	@JsonProperty("IT_RETURNTAB")
	private Object itReturnTab; //Not of any use, just putting for fulfilling payload's requirement
	
	@JsonProperty("IT_RET_MSG")
	private Object itRetMsg; //Not of any use, just putting for fulfilling payload's requirement
	
	public ItCustomer getItCustomer() {
		return itCustomer;
	}

	public void setItCustomer(ItCustomer itCustomer) {
		this.itCustomer = itCustomer;
	}

	public Object getItReturnTab() {
		return itReturnTab;
	}

	public void setItReturnTab(Object itReturnTab) {
		this.itReturnTab = itReturnTab;
	}

	public Object getItRetMsg() {
		return itRetMsg;
	}

	public void setItRetMsg(Object itRetMsg) {
		this.itRetMsg = itRetMsg;
	}
	
	
}
