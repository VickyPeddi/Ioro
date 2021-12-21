package com.iocl.integratedunblockingboot.payload;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItCustomer {

	@JsonProperty("item")
	private ArrayList<ItCustomerItem> itemList;

	@Override
	public String toString() {
		return "ItCustomer [item=" + itemList + "]";
	}

	public ArrayList<ItCustomerItem> getItem() {
		return itemList;
	}

	public void setItem(ArrayList<ItCustomerItem> item) {
		this.itemList = item;
	}

	
	
	
	
}
