package com.bluerizon.hcmanager.payload.response;

import com.bluerizon.hcmanager.models.Entreprises;
import com.bluerizon.hcmanager.models.Factures;
import com.bluerizon.hcmanager.models.TypeTraitements;

import java.util.List;

public class EtatFactureEntreprise {

    private Entreprises entreprise;
    private List<EtatFacturePatient> facturePatients;

    public EtatFactureEntreprise() {
    }

    public Entreprises getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprises entreprise) {
        this.entreprise = entreprise;
    }

    public List<EtatFacturePatient> getFacturePatients() {
        return facturePatients;
    }

    public void setFacturePatients(List<EtatFacturePatient> facturePatients) {
        this.facturePatients = facturePatients;
    }
}
