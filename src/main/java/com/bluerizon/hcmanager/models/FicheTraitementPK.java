// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.models;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FicheTraitementPK implements Serializable
{
    @JoinColumn(name = "fiche", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Fiches fiche;
    @JoinColumn(name = "traitement", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Traitements traitement;

    public FicheTraitementPK() {
    }

    public FicheTraitementPK(Fiches fiche, Traitements traitement) {
        this.fiche = fiche;
        this.traitement = traitement;
    }

    public Fiches getFiche() {
        return fiche;
    }

    public void setFiche(Fiches fiche) {
        this.fiche = fiche;
    }

    public Traitements getTraitement() {
        return traitement;
    }

    public void setTraitement(Traitements traitement) {
        this.traitement = traitement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FicheTraitementPK that = (FicheTraitementPK) o;
        return Objects.equals(fiche, that.fiche) && Objects.equals(traitement, that.traitement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fiche, traitement);
    }

    @Override
    public String toString() {
        return "FicheTraitementPK{" +
                "fiche=" + fiche +
                ", traitement=" + traitement +
                '}';
    }
}
