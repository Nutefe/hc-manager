package com.bluerizon.hcmanager.payload.response;

import com.bluerizon.hcmanager.models.Patients;
import com.bluerizon.hcmanager.models.Traitements;
import com.bluerizon.hcmanager.models.Utilisateurs;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

public class FicheResponse {
    private Utilisateurs utilisateur;
    private Patients patient;
    private Date dateFiche;
    private List<FTraitementResponse> traitements;

    public FicheResponse() {
    }

    public Utilisateurs getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateurs utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Patients getPatient() {
        return patient;
    }

    public void setPatient(Patients patient) {
        this.patient = patient;
    }

    public Date getDateFiche() {
        return dateFiche;
    }

    public void setDateFiche(Date dateFiche) {
        this.dateFiche = dateFiche;
    }

    public List<FTraitementResponse> getTraitements() {
        return traitements;
    }

    public void setTraitements(List<FTraitementResponse> traitements) {
        this.traitements = traitements;
    }
}
