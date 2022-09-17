package com.bluerizon.hcmanager.payload.response;

import com.bluerizon.hcmanager.payload.pages.FacturePage;

public class EtatRecette {

    private Double montantRecette;
    private Double montantDecaissement;
    private Double montantReserve;
    private Double montantDepense;

    public EtatRecette() {
    }

    public Double getMontantRecette() {
        return montantRecette;
    }

    public void setMontantRecette(Double montantRecette) {
        this.montantRecette = montantRecette;
    }

    public Double getMontantDecaissement() {
        return montantDecaissement;
    }

    public void setMontantDecaissement(Double montantDecaissement) {
        this.montantDecaissement = montantDecaissement;
    }

    public Double getMontantReserve() {
        return montantReserve;
    }

    public void setMontantReserve(Double montantReserve) {
        this.montantReserve = montantReserve;
    }

    public Double getMontantDepense() {
        return montantDepense;
    }

    public void setMontantDepense(Double montantDepense) {
        this.montantDepense = montantDepense;
    }
}
