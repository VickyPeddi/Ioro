package com.iocl.integratedunblockingboot.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkUnblockResponsePayload {

	@JsonProperty("IT_RET_MSG")
	private ItRetMsg itRetMsg;

	public ItRetMsg getItRetMsg() {
		return itRetMsg;
	}

	public void setItRetMsg(ItRetMsg itRetMsg) {
		this.itRetMsg = itRetMsg;
	}

	@Override
	public String toString() {
		return "BulkUnblockResponsePayload [itRetMsg=" + itRetMsg + "]";
	}
	
	
}
