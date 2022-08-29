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
public class CaissePK implements Serializable
{
    @JoinColumn(name = "caisse", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Caisses caisse;
    @JoinColumn(name = "utilisateur", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Utilisateurs utilisateur;

    public CaissePK() {
    }

    public CaissePK(Caisses caisse, Utilisateurs utilisateur) {
        this.caisse = caisse;
        this.utilisateur = utilisateur;
    }

    public Caisses getCaisse() {
        return caisse;
    }

    public void setCaisse(Caisses caisse) {
        this.caisse = caisse;
    }

    public Utilisateurs getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateurs utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CaissePK caissePK = (CaissePK) o;
        return Objects.equals(caisse, caissePK.caisse) && Objects.equals(utilisateur, caissePK.utilisateur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caisse, utilisateur);
    }

    @Override
    public String toString() {
        return "caissePK{" +
                "caisse=" + caisse +
                ", utilisateur=" + utilisateur +
                '}';
    }
}
