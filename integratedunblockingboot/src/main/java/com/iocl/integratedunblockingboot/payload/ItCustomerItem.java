package com.iocl.integratedunblockingboot.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItCustomerItem {

//	"KUNNR": "0000102082",
//    "VKORG": 1100,
//    "VTWEG": "RE",
//    "SPART": "MH",
//    "P_ORD_BLK": 12,
//    "P_DEL_BLK": 12,
//    "P_BILL_BLK": 12
	@JsonProperty("KUNNR")
	String kunnr;
	
	@JsonProperty("VKORG")
	int vkorg;
	
	@JsonProperty("VTWEG")
	String vtweg;
	
	@JsonProperty("SPART")
	String spart;
	
	@JsonProperty("P_ORD_BLK")
	int pOrdBlk;
	
	@JsonProperty("P_DEL_BLK")
	int pDelBlk;
	
	@JsonProperty("P_BILL_BLK")
	int pBillBlk;

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

	public int getpOrdBlk() {
		return pOrdBlk;
	}

	public void setpOrdBlk(int pOrdBlk) {
		this.pOrdBlk = pOrdBlk;
	}

	public int getpDelBlk() {
		return pDelBlk;
	}

	public void setpDelBlk(int pDelBlk) {
		this.pDelBlk = pDelBlk;
	}

	public int getpBillBlk() {
		return pBillBlk;
	}

	public void setpBillBlk(int pBillBlk) {
		this.pBillBlk = pBillBlk;
	}
	
	
	
	
}
