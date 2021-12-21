package com.iocl.integratedunblockingboot.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReturnItem {

	 
//         "KUNNR": "0000102082",
//         "VKORG": 1100,
//         "VTWEG": "RE",
//         "SPART": "MH",
//         "RET_MSG": "CUSTOMER IS ALREADY UNBLOCKED IN SALES AREA!!"
     
	@JsonProperty("KUNNR")
	String kunnr;
	
	@JsonProperty("VKORG")
	int vkorg;
	
	@JsonProperty("VTWEG")
	String vtweg;
	
	@JsonProperty("SPART")
	String spart;
	
	@JsonProperty("RET_MSG")
	String retMsg;

	public String getKunnr() {
		return kunnr;
	}

	public void setKunnr(String kunnr) {
		this.kunnr = kunnr;
	}

	public int getVkorg() {
		return vkorg;
	}

	public void setVkorg(int vkorg) {
		this.vkorg = vkorg;
	}

	public String getVtweg() {
		return vtweg;
	}

	public void setVtweg(String vtweg) {
		this.vtweg = vtweg;
	}

	public String getSpart() {
		return spart;
	}

	public void setSpart(String spart) {
		this.spart = spart;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	@Override
	public String toString() {
		return "ReturnItem [kunnr=" + kunnr + ", vkorg=" + vkorg + ", vtweg=" + vtweg + ", spart=" + spart + ", retMsg="
				+ retMsg + "]";
	}
	
	
	
}
