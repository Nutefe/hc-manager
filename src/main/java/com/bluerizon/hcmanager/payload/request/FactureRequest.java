package com.bluerizon.hcmanager.payload.request;

import com.bluerizon.hcmanager.models.Kotas;
import com.bluerizon.hcmanager.models.Patients;
import com.bluerizon.hcmanager.models.Traitements;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.util.List;

public class FactureRequest {

	private Patients patient;
	private List<TraitementRequest> traitements;
	private boolean unite;
	private Double accompte;
	private Double remise;

	public FactureRequest() {
	}

	public Patients getPatient() {
		return patient;
	}

	public void setPatient(Patients patient) {
		this.patient = patient;
	}

	public List<TraitementRequest> getTraitements() {
		return traitements;
	}

	public void setTraitements(List<TraitementRequest> traitements) {
		this.traitements = traitements;
	}

	public boolean isUnite() {
		return unite;
	}

	public void setUnite(boolean unite) {
		this.unite = unite;
	}

	public Double getRemise() {
		return remise;
	}

	public void setRemise(Double remise) {
		this.remise = remise;
	}

	public Double getAccompte() {
		return accompte;
	}

	public void setAccompte(Double accompte) {
		this.accompte = accompte;
	}

}
