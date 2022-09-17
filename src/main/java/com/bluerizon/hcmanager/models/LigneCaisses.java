// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ligneCaisses")
@XmlRootElement
@EntityListeners({ AuditingEntityListener.class })
public class LigneCaisses implements Serializable
{
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CaissePK caissePK;
//    @Column(name = "solde")
//    private Double solde;
//    @Column(name = "recette")
//    private Double recette;
//    @Column(name = "decaissement")
//    private Double decaissement;
    @Column(name = "deleted")
    private boolean deleted;
    @Version
    @Basic(optional = false)
    @Column(nullable = false)
    private int version;
    @CreatedDate
    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public LigneCaisses() {
        this.deleted = false;
    }

    public LigneCaisses(CaissePK caissePK) {
        this.caissePK = caissePK;
    }

    public CaissePK getCaissePK() {
        return caissePK;
    }

    public void setCaissePK(CaissePK caissePK) {
        this.caissePK = caissePK;
    }

//    public Double getSolde() {
//        return solde;
//    }
//
//    public void setSolde(Double solde) {
//        this.solde = solde;
//    }
//
//    public Double getRecette() {
//        return recette;
//    }
//
//    public void setRecette(Double recette) {
//        this.recette = recette;
//    }
//
//    public Double getDecaissement() {
//        return decaissement;
//    }
//
//    public void setDecaissement(Double decaissement) {
//        this.decaissement = decaissement;
//    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LigneCaisses that = (LigneCaisses) o;
        return deleted == that.deleted && version == that.version && Objects.equals(caissePK, that.caissePK) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caissePK, deleted, version, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "LigneCaisses{" +
                "caissePK=" + caissePK +
                ", deleted=" + deleted +
                ", version=" + version +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
