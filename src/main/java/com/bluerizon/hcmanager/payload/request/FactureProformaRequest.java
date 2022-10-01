package com.bluerizon.hcmanager.payload.request;

import com.bluerizon.hcmanager.models.Patients;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

public class FactureProformaRequest {

	private String nom;
	private String prenom;
	private String dateNaiss;
	private List<TraitementProformaRequest> traitements;

	public FactureProformaRequest() {
	}

	public FactureProformaRequest(String nom,
								  String prenom,
								  String dateNaiss,
								  List<TraitementProformaRequest> traitements) {
		this.nom = nom;
		this.prenom = prenom;
		this.dateNaiss = dateNaiss;
		this.traitements = traitements;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getDateNaiss() {
		return dateNaiss;
	}

	public void setDateNaiss(String dateNaiss) {
		this.dateNaiss = dateNaiss;
	}

	public List<TraitementProformaRequest> getTraitements() {
		return traitements;
	}

	public void setTraitements(List<TraitementProformaRequest> traitements) {
		this.traitements = traitements;
	}
}
