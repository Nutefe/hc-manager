package com.bluerizon.hcmanager.payload.request;

import com.bluerizon.hcmanager.models.Traitements;

import javax.persistence.Column;

public class TraitementProformaRequest {

	private String traitement;
	private String description;
	private Double price;

	public TraitementProformaRequest() {
	}

	public TraitementProformaRequest(String traitement, String description, Double price) {
		this.traitement = traitement;
		this.description = description;
		this.price = price;
	}

	public String getTraitement() {
		return traitement;
	}

	public void setTraitement(String traitement) {
		this.traitement = traitement;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}
