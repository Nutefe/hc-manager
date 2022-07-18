package com.bluerizon.hcmanager.payload.request;

import javax.validation.constraints.Pattern;

public class CodeRequest {
	private String code;

	public CodeRequest() {
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
