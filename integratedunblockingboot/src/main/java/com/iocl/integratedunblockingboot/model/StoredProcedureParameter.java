package com.iocl.integratedunblockingboot.model;

import javax.persistence.ParameterMode;

public class StoredProcedureParameter {

	private String parameterName;

	@SuppressWarnings("rawtypes")
	private Class classType;

	private ParameterMode mode;
	
	private Object parameterValue;

	public Object getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(Object parameterValue) {
		this.parameterValue = parameterValue;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	@SuppressWarnings("rawtypes")
	public Class getClassType() {
		return classType;
	}

	@SuppressWarnings("rawtypes")
	public void setClassType(Class classType) {
		this.classType = classType;
	}

	public ParameterMode getMode() {
		return mode;
	}

	public void setMode(ParameterMode mode) {
		this.mode = mode;
	}

	@SuppressWarnings("rawtypes")
	public StoredProcedureParameter(String parameterName, Class classType, ParameterMode mode, Object parameterValue) {
		super();
		this.parameterName = parameterName;
		this.classType = classType;
		this.mode = mode;
		this.parameterValue = parameterValue;
	}

}
