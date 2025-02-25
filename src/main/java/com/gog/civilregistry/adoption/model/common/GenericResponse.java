package com.gog.civilregistry.adoption.model.common;

public class GenericResponse<T> extends ServiceResponse {
	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
