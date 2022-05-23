package com.bluerizon.hcmanager.payload.request;

import javax.validation.constraints.Pattern;

public class PasswordRequest {

	@Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,40}$",
			message="regex controle")
	private String nouveau;

	public String getNouveau() {
		return nouveau;
	}

	public void setNouveau(String nouveau) {
		this.nouveau = nouveau;
	}
}
