package com.iocl.integratedunblockingboot.dao;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.iocl.integratedunblockingboot.model.SapUnblockCustomers;
import com.iocl.integratedunblockingboot.model.SapUnblockCustomersId;

@Transactional
public interface SapUnblockCustomersDAO extends CrudRepository<SapUnblockCustomers, SapUnblockCustomersId>{

	ArrayList<SapUnblockCustomers> findTop1000ByUnblockStatus(String unblockStatus);
	
	@Query("update SapUnblockCustomers set unblockStatus= :unblockStatus,sapUnblockMsg=:sapUnblockMsg,dateUpdated = sysdate, unblockConfDateTime = sysdate where unblockAttemptNo=:unblockAttemptNo and "
			+ "blockId = :blockId")
	int updateUnblockingStatus(@Param("blockId") String blockId,@Param("unblockAttemptNo")int unblockAttemptNo
			,@Param("unblockStatus") String unblockStatus,@Param("sapUnblockMsg") String sapUnblockMsg); 
	
	@Query(name="UPDATE SAP_BLOCK_CUSTOMER_DETAILS SET ROW_UNBLOCK_DATETIME=sysdate ,UPDATE_DATETIME = sysdate where BLOCK_ID=:blockId"
			,nativeQuery = true)
	int updateSapBlockCustomerDetails(@Param("blockId") String blockId);
}
