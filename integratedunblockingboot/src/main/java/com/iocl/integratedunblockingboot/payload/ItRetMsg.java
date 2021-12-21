package com.iocl.integratedunblockingboot.payload;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItRetMsg {

//	 {
//         "KUNNR": "0000102082",
//         "VKORG": 1100,
//         "VTWEG": "RE",
//         "SPART": "MH",
//         "RET_MSG": "CUSTOMER IS ALREADY UNBLOCKED IN SALES AREA!!"
//     }
	@JsonProperty("item")
	private ArrayList<ReturnItem> itemList;

	@Override
	public String toString() {
		return "ItRetMsg [itemList=" + itemList + "]";
	}

	public ArrayList<ReturnItem> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<ReturnItem> itemList) {
		this.itemList = itemList;
	}

	

	

	
}
