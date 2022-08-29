package com.bluerizon.hcmanager.payload.response;

import com.bluerizon.hcmanager.models.Factures;
import com.bluerizon.hcmanager.models.FicheTraitements;
import com.bluerizon.hcmanager.models.TypeTraitements;
import com.bluerizon.hcmanager.payload.pages.FacturePage;

import java.util.List;

public class EtatEncaissemnt {

    private Double montant;
    private Long patient;
    private Long facture;
    private Long encaisse;
    private FacturePage page;

    public EtatEncaissemnt() {
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Long getPatient() {
        return patient;
    }

    public void setPatient(Long patient) {
        this.patient = patient;
    }

    public Long getFacture() {
        return facture;
    }

    public void setFacture(Long facture) {
        this.facture = facture;
    }

    public Long getEncaisse() {
        return encaisse;
    }

    public void setEncaisse(Long encaisse) {
        this.encaisse = encaisse;
    }

    public FacturePage getPage() {
        return page;
    }

    public void setPage(FacturePage page) {
        this.page = page;
    }
}
