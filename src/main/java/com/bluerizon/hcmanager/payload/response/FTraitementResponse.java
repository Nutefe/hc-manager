package com.bluerizon.hcmanager.payload.response;

import com.bluerizon.hcmanager.models.Kotas;

import javax.persistence.Column;

public class FTraitementResponse {
    private Kotas kota;
    private Double taux;
    private boolean unite;
    private Double netPayAssu;
    private Double baseRembours;
    private Double netPayBeneficiaire;

    public FTraitementResponse() {
    }

    public FTraitementResponse(Kotas kota, Double taux, boolean unite, Double netPayAssu, Double baseRembours, Double netPayBeneficiaire) {
        this.kota = kota;
        this.taux = taux;
        this.unite = unite;
        this.netPayAssu = netPayAssu;
        this.baseRembours = baseRembours;
        this.netPayBeneficiaire = netPayBeneficiaire;
    }

    public Kotas getKota() {
        return kota;
    }

    public void setKota(Kotas kota) {
        this.kota = kota;
    }

    public Double getTaux() {
        return taux;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public boolean isUnite() {
        return unite;
    }

    public void setUnite(boolean unite) {
        this.unite = unite;
    }

    public Double getNetPayAssu() {
        return netPayAssu;
    }

    public void setNetPayAssu(Double netPayAssu) {
        this.netPayAssu = netPayAssu;
    }

    public Double getBaseRembours() {
        return baseRembours;
    }

    public void setBaseRembours(Double baseRembours) {
        this.baseRembours = baseRembours;
    }

    public Double getNetPayBeneficiaire() {
        return netPayBeneficiaire;
    }

    public void setNetPayBeneficiaire(Double netPayBeneficiaire) {
        this.netPayBeneficiaire = netPayBeneficiaire;
    }
}
