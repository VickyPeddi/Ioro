package com.iocl.integratedunblockingboot.dao;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.iocl.integratedunblockingboot.model.StoredProcedureParameter;


@Transactional
@Component
@SuppressWarnings("rawtypes")
public class GenericProcedureCallDAO {

	@Autowired
	private EntityManager entityManager;

	public Object callProcAndReturnObejct(String procedureName, Class procedureModel, StoredProcedureParameter param) {
		System.out.println("Proc started at : " + LocalDateTime.now());
		StoredProcedureQuery procedureQuery = entityManager.createStoredProcedureQuery(procedureName, procedureModel);
		procedureQuery.registerStoredProcedureParameter("insp_cursor", void.class, ParameterMode.REF_CURSOR);
		procedureQuery.registerStoredProcedureParameter(param.getParameterName(), param.getClassType(),
				param.getMode());
		procedureQuery.setParameter(param.getParameterName(), param.getParameterValue());
		procedureQuery.execute();
		Object returnedObject = procedureQuery.getResultList().get(0);
		System.out.println("Proc ended at : " + LocalDateTime.now());

		return returnedObject;
	}

	public Object callProcAndReturnOutParam(String procedureName, String outParamName, Class outParamDataType,
			StoredProcedureParameter param) {
		System.out.println("Proc started at : " + LocalDateTime.now());

		StoredProcedureQuery procedureQuery = entityManager.createStoredProcedureQuery(procedureName);
		procedureQuery.registerStoredProcedureParameter(outParamName, outParamDataType, ParameterMode.OUT);
		procedureQuery.registerStoredProcedureParameter(param.getParameterName(), param.getClassType(),
				param.getMode());
		procedureQuery.setParameter(param.getParameterName(), param.getParameterValue());
		Object returnedObject = procedureQuery.getOutputParameterValue(outParamName);
		System.out.println("Proc ended at : " + LocalDateTime.now());
		return returnedObject;
	}

	public Object callProcAndReturnOutParam(String procedureName, String outParamName, Class outParamDataType,
			List<StoredProcedureParameter> parameterList) {
		System.out.println("Proc started at : " + LocalDateTime.now());

		StoredProcedureQuery procedureQuery = entityManager.createStoredProcedureQuery(procedureName);
		procedureQuery.registerStoredProcedureParameter(outParamName, outParamDataType, ParameterMode.OUT);
		parameterList.forEach(param -> {
			procedureQuery.registerStoredProcedureParameter(param.getParameterName(), param.getClassType(),
					param.getMode());
			procedureQuery.setParameter(param.getParameterName(), param.getParameterValue());
		});
		Object returnedObject = procedureQuery.getOutputParameterValue(outParamName);
		System.out.println("Proc ended at : " + LocalDateTime.now());
		return returnedObject;
	}

	// This is a generic method which will be called for all procedures, just keep
	// in mind that all procedures should have "insp_cursor" as cursor

	public List<?> callProcAndReturnList(String procedureName, Class procedureModel,
			List<StoredProcedureParameter> parameterList) {
		System.out.println("Proc started at : " + LocalDateTime.now());

		StoredProcedureQuery procedureQuery = entityManager.createStoredProcedureQuery(procedureName, procedureModel);
		procedureQuery.registerStoredProcedureParameter("insp_cursor", void.class, ParameterMode.REF_CURSOR);
		parameterList.forEach(param -> {
			procedureQuery.registerStoredProcedureParameter(param.getParameterName(), param.getClassType(),
					param.getMode());
			procedureQuery.setParameter(param.getParameterName(), param.getParameterValue());
		});
		List<?> returnedList = procedureQuery.getResultList();
		System.out.println("Proc ended at : " + LocalDateTime.now());
		return returnedList;

	}

	public List<?> callProcAndReturnList(String procedureName, Class procedureModel, StoredProcedureParameter param) {
		System.out.println("Proc started at : " + LocalDateTime.now());

		StoredProcedureQuery procedureQuery = entityManager.createStoredProcedureQuery(procedureName, procedureModel);
		procedureQuery.registerStoredProcedureParameter("insp_cursor", void.class, ParameterMode.REF_CURSOR);
		procedureQuery.registerStoredProcedureParameter(param.getParameterName(), param.getClassType(),
				param.getMode());
		procedureQuery.setParameter(param.getParameterName(), param.getParameterValue());
		List<?> returnedList = procedureQuery.getResultList();
		System.out.println("Proc ended at : " + LocalDateTime.now());
		return returnedList;
	}

	public void callProcedureWithNoReturnValue(String procedureName, StoredProcedureParameter param) {
		System.out.println("Proc started at : " + LocalDateTime.now());

		StoredProcedureQuery procedureQuery = entityManager.createStoredProcedureQuery(procedureName);
		procedureQuery.registerStoredProcedureParameter(param.getParameterName(), param.getClassType(),
				param.getMode());
		procedureQuery.setParameter(param.getParameterName(), param.getParameterValue());
		procedureQuery.execute();
		System.out.println("Proc ended at : " + LocalDateTime.now());

	}
	
	public void callProcedureWithNoInputOutput(String procedureName) {
		System.out.println("Proc started at : " + LocalDateTime.now());

		StoredProcedureQuery procedureQuery = entityManager.createStoredProcedureQuery(procedureName);	
		procedureQuery.execute();
		System.out.println("Proc ended at : " + LocalDateTime.now());

	}
	
	
	
	public void callProcedureWithNoReturnValue(String procedureName, List<StoredProcedureParameter> parameterList) {
		System.out.println("Proc started at : " + LocalDateTime.now());
		StoredProcedureQuery procedureQuery = entityManager.createStoredProcedureQuery(procedureName);
		parameterList.forEach(param -> {
			procedureQuery.registerStoredProcedureParameter(param.getParameterName(), param.getClassType(),
					param.getMode());
			procedureQuery.setParameter(param.getParameterName(), param.getParameterValue());
		});
		procedureQuery.execute();
		System.out.println("Proc ended at : " + LocalDateTime.now());
	}

}
