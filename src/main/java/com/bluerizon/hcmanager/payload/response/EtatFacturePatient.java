package com.bluerizon.hcmanager.payload.response;

import com.bluerizon.hcmanager.models.Factures;
import com.bluerizon.hcmanager.models.FicheTraitements;
import com.bluerizon.hcmanager.models.TypeTraitements;

import java.util.List;

public class EtatFacturePatient {

    private Factures facture;
    private List<FicheTraitements> fiches;
    private List<TypeTraitements> types;

    public EtatFacturePatient() {
    }

    public Factures getFacture() {
        return facture;
    }

    public List<FicheTraitements> getFiches() {
        return fiches;
    }

    public void setFiches(List<FicheTraitements> fiches) {
        this.fiches = fiches;
    }

    public void setFacture(Factures facture) {
        this.facture = facture;
    }

    public List<TypeTraitements> getTypes() {
        return types;
    }

    public void setTypes(List<TypeTraitements> types) {
        this.types = types;
    }
}
