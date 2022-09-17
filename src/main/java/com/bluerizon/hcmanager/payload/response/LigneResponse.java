package com.bluerizon.hcmanager.payload.response;

import com.bluerizon.hcmanager.models.Caisses;
import com.bluerizon.hcmanager.models.Utilisateurs;

import java.util.List;

public class LigneResponse {
    private Caisses caisse;
    private List<Utilisateurs> utilisateurs;

    public LigneResponse() {
    }

    public LigneResponse(Caisses caisse, List<Utilisateurs> utilisateurs) {
        this.caisse = caisse;
        this.utilisateurs = utilisateurs;
    }

    public Caisses getCaisse() {
        return caisse;
    }

    public void setCaisse(Caisses caisse) {
        this.caisse = caisse;
    }

    public List<Utilisateurs> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(List<Utilisateurs> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }
}
