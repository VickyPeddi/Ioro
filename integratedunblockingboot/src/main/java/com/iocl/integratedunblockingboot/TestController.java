package com.iocl.integratedunblockingboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iocl.integratedunblockingboot.payload.BulkUnblockRequestPayLoad;
import com.iocl.integratedunblockingboot.payload.ItCustomer;
import com.iocl.integratedunblockingboot.payload.ItCustomerItem;

@RestController
public class TestController {

//	@GetMapping("/request-payload-josn")
//	public BulkUnblockRequestPayLoad getRequestPayload() {
//		BulkUnblockRequestPayLoad requestPaylod = new 	BulkUnblockRequestPayLoad();
//		ItCustomer itCustomer = new ItCustomer();
//		ItCustomerItem item = new ItCustomerItem();
//		item.setKunnr("0000178895");
//		item.setpOrdBlk(11);
//		item.setpDelBlk(10);
//		item.setpBillBlk(10);
//		item.setSpart("MH");
//		item.setVtweg("RE");
//		item.setVkorg(3300);
//		itCustomer.setItem(item);
//		requestPaylod.setItCustomer(itCustomer);
//		return requestPaylod;
//	}
	
}
