package com.bluerizon.hcmanager.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class PasswordConnectRequest {

	@NotBlank
	private String ancien;
	@Pattern(regexp=" ^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,40}$",
			message="regex controle")
	private String nouveau;

	public String getAncien() {
		return ancien;
	}

	public void setAncien(String ancien) {
		this.ancien = ancien;
	}

	public String getNouveau() {
		return nouveau;
	}

	public void setNouveau(String nouveau) {
		this.nouveau = nouveau;
	}
}
