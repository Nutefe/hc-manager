package com.bluerizon.hcmanager.payload.response;

import com.bluerizon.hcmanager.models.Kotas;
import com.bluerizon.hcmanager.models.Traitements;

import javax.persistence.Column;

public class FTraitementResponse {
    private Kotas kota;
    private boolean unite;
    private Double netPayAssu;
    private Double baseRembours;
    private Double netPayBeneficiaire;
    private Traitements traitement;
    public FTraitementResponse() {
    }

    public FTraitementResponse(Kotas kota, boolean unite, Double netPayAssu, Double baseRembours, Double netPayBeneficiaire) {
        this.kota = kota;
        this.unite = unite;
        this.netPayAssu = netPayAssu;
        this.baseRembours = baseRembours;
        this.netPayBeneficiaire = netPayBeneficiaire;
    }

    public FTraitementResponse(Kotas kota, boolean unite, Double netPayAssu, Double baseRembours, Double netPayBeneficiaire, Traitements traitement) {
        this.kota = kota;
        this.unite = unite;
        this.netPayAssu = netPayAssu;
        this.baseRembours = baseRembours;
        this.netPayBeneficiaire = netPayBeneficiaire;
        this.traitement = traitement;
    }

    public Kotas getKota() {
        return kota;
    }

    public void setKota(Kotas kota) {
        this.kota = kota;
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

    public Traitements getTraitement() {
        return traitement;
    }

    public void setTraitement(Traitements traitement) {
        this.traitement = traitement;
    }
}
