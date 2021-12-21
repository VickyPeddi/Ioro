package com.iocl.integratedunblockingboot.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.ParameterMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.iocl.integratedunblockingboot.dao.GenericProcedureCallDAO;
import com.iocl.integratedunblockingboot.dao.SapUnblockCustomersDAO;
import com.iocl.integratedunblockingboot.model.SapUnblockCustomers;
import com.iocl.integratedunblockingboot.model.SapUnblockCustomersId;
import com.iocl.integratedunblockingboot.model.StoredProcedureParameter;
import com.iocl.integratedunblockingboot.payload.BulkUnblockRequestPayLoad;
import com.iocl.integratedunblockingboot.payload.BulkUnblockResponsePayload;
import com.iocl.integratedunblockingboot.payload.ItCustomer;
import com.iocl.integratedunblockingboot.payload.ItCustomerItem;
import com.iocl.integratedunblockingboot.payload.ReturnItem;

@Service
public class UnblockService {

	@Autowired
	SapUnblockCustomersDAO sapUnblockCustomersDAO;
	
	@Autowired
	GenericProcedureCallDAO procedureCallDAO;

	@Autowired
	RestTemplate restTemplate;

	public void unblockCustomers() {
		
		procedureCallDAO.callProcedureWithNoInputOutput("CUSTOMER_UNBLOCKING");
		
		HashMap<Integer, String> custcodeBlockIdMap = new HashMap<>();
		
		ArrayList<SapUnblockCustomers> tobeUnblockedList = sapUnblockCustomersDAO.findTop1000ByUnblockStatus("P");
		BulkUnblockRequestPayLoad requestPaylod = new BulkUnblockRequestPayLoad();
		ItCustomer itCustomer = new ItCustomer();
		ArrayList<ItCustomerItem> customerList = new ArrayList<>();
		tobeUnblockedList.forEach(customer -> {
			try {
//				
				ItCustomerItem item = new ItCustomerItem();
				item.setKunnr("0000" + customer.getCustcode());
				item.setpOrdBlk(11);
				item.setpDelBlk(10);
				item.setpBillBlk(10);
				item.setSpart(customer.getDivision());
				item.setVtweg("RE");
				item.setVkorg(customer.getSalesorg());
				customerList.add(item);
				customer.setUnblockStatus("I");
				customer.setDateUpdated(LocalDateTime.now());
				sapUnblockCustomersDAO.save(customer);
				
				custcodeBlockIdMap.put(customer.getCustcode(), customer.getBlockId()+"##"+customer.getUnblockAttemptNo());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		itCustomer.setItem(customerList);
		requestPaylod.setItCustomer(itCustomer);
		restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor("mdq200_b2b_user", "mdq@1234"));
		
		System.out.println("Requestpayload for unblocking : ");
		System.out.println(requestPaylod);
		System.out.println("API for unblocking called at : "+LocalDateTime.now());
		
		BulkUnblockResponsePayload response = restTemplate.postForObject(
				"https://coisebizuat.ds.indianoil.in:7000/RESTAdapter/RFC/YV_IF_CUST_SALE_UNBLOCK_BULK",
				requestPaylod, BulkUnblockResponsePayload.class);	
		System.out.println("API call for unblocking completed at : "+LocalDateTime.now());

		ArrayList<ReturnItem> returnedList = response.getItRetMsg().getItemList();
		returnedList.forEach(returnItem->{
			String message= returnItem.getRetMsg();
			System.out.println("RO Code: "+returnItem.getKunnr()+" : "+message);
			String mapText = custcodeBlockIdMap.get(Integer.parseInt(returnItem.getKunnr()));
			String blockId = mapText.split("##")[0];
			int unblockingAttemptNo = Integer.parseInt(mapText.split("##")[1]);
			
			if(message.trim().toLowerCase().contains("order unblocked") || message.trim().equalsIgnoreCase("Custmer is already Unblocked in sales area!!")) {				
				if(mapText!=null&&mapText.split("##").length>0) {					
					sapUnblockCustomersDAO.updateUnblockingStatus(blockId, unblockingAttemptNo, "S",message);
					sapUnblockCustomersDAO.updateSapBlockCustomerDetails(blockId);
				}
				
			}else {
				sapUnblockCustomersDAO.updateUnblockingStatus(blockId, unblockingAttemptNo, "R",message);
				ArrayList<StoredProcedureParameter> parameterList = new ArrayList<>();
				parameterList.add(new StoredProcedureParameter("BLOCK_ID", String.class, ParameterMode.IN, blockId));
				parameterList.add(new StoredProcedureParameter("UNBLOCK_ATTEMPT_NO", Integer.class, ParameterMode.IN, unblockingAttemptNo));
				procedureCallDAO.callProcedureWithNoReturnValue("PROC_REJECTED_ON_UNBLOCKING", parameterList);
			}
//				.trim().equalsIgnoreCase("Order Unblocked!!Delivery Unblocked!!Billing Unblocked!!") || message[j].trim().equalsIgnoreCase("Custmer is already Unblocked in sales area!!")
		});
		
	}
	
	
	//-------------------------------------------------------------Sample Request---------------------------------------------//
//	{
//	    "IT_CUSTOMER": {
//	        "item":
//	        [ {
//	            "KUNNR": "0000178895",
//	            "VKORG": 1300,
//	            "VTWEG": "RE",
//	            "SPART": "MH",
//	            "P_ORD_BLK": 11
//	            
//	        },
//	        {
//	            "KUNNR": "0000102082",
//	            "VKORG": 1100,
//	            "VTWEG": "RE",
//	            "SPART": "MH",
//	            "P_ORD_BLK": 11
//	            
//	        }
//	        ]
//	    },
//	    "IT_RETURNTAB": null,
//	    "IT_RET_MSG": null
//	    
//	}
	
	//------------------------------------------------------------------sample response--------------------------------------------
	
//	{
//	    "MT_YV_IF_CUST_SALE_UNBLOCK_BULK_RES": {
//	        "IT_CUSTOMER": {
//	            "item": [
//	                {
//	                    "KUNNR": "0000178895",
//	                    "VKORG": 1300,
//	                    "VTWEG": "RE",
//	                    "SPART": "MH",
//	                    "P_ORD_BLK": 11,
//	                    "P_DEL_BLK": "",
//	                    "P_BILL_BLK": ""
//	                },
//	                {
//	                    "KUNNR": "0000102082",
//	                    "VKORG": 1100,
//	                    "VTWEG": "RE",
//	                    "SPART": "MH",
//	                    "P_ORD_BLK": 11,
//	                    "P_DEL_BLK": "",
//	                    "P_BILL_BLK": ""
//	                }
//	            ]
//	        },
//	        "IT_RETURNTAB": "",
//	        "IT_RET_MSG": {
//	            "item": [
//	                {
//	                    "KUNNR": "0000178895",
//	                    "VKORG": 1300,
//	                    "VTWEG": "RE",
//	                    "SPART": "MH",
//	                    "RET_MSG": "ORDER BLOCKING REASON NOT MATCHING."
//	                },
//	                {
//	                    "KUNNR": "0000102082",
//	                    "VKORG": 1100,
//	                    "VTWEG": "RE",
//	                    "SPART": "MH",
//	                    "RET_MSG": "CUSTOMER IS ALREADY UNBLOCKED IN SALES AREA!!"
//	                }
//	            ]
//	        }
//	    }
//	}
	
}
