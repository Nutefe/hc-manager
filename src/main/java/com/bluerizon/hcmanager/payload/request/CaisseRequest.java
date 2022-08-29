package com.bluerizon.hcmanager.payload.request;

import com.bluerizon.hcmanager.models.Patients;
import com.bluerizon.hcmanager.models.Utilisateurs;

import javax.persistence.Column;
import java.util.List;

public class CaisseRequest {

	private Utilisateurs utilisateur;
	private String libelle;
	private Double montant;

	public CaisseRequest() {
	}

	public Utilisateurs getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateurs utilisateur) {
		this.utilisateur = utilisateur;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Double getMontant() {
		return montant;
	}

	public void setMontant(Double montant) {
		this.montant = montant;
	}
}
