package com.bluerizon.hcmanager.payload.request;

import com.bluerizon.hcmanager.models.Patients;
import com.bluerizon.hcmanager.models.Traitements;

import java.util.List;

public class TraitementRequest {

	private Traitements traitement;
	private String kota;
	private Double baseRembour;
	private Double netAssurance;

	public TraitementRequest() {
	}

	public Traitements getTraitement() {
		return traitement;
	}

	public void setTraitement(Traitements traitement) {
		this.traitement = traitement;
	}

	public String getKota() {
		return kota;
	}

	public void setKota(String kota) {
		this.kota = kota;
	}

	public Double getBaseRembour() {
		return baseRembour;
	}

	public void setBaseRembour(Double baseRembour) {
		this.baseRembour = baseRembour;
	}

	public Double getNetAssurance() {
		return netAssurance;
	}

	public void setNetAssurance(Double netAssurance) {
		this.netAssurance = netAssurance;
	}
}
