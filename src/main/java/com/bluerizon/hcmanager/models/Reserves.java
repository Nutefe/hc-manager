// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "reserves")
@JsonIgnoreProperties(value = {"updatedAt",  "deleted"})
@EntityListeners({ AuditingEntityListener.class })
public class Reserves implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne
    private Caisses caisse;
    @Column(name = "libelle", length = 50)
    private String libelle;
    @Column(name = "jours")
    private Integer jours;
    @Column(name = "heure")
    private String heure;
    @Column(name = "montantDefini")
    private Double montantDefini;
    @Column(name = "montantReserve")
    private Double montantReserve;

    @Column(name = "montantSuivant")
    private Double montantSuivant;

    @Column(name = "dateReserve")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateReserve;
    @Column(name = "dateSuivant")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateSuivant;
    @Column(name = "deleted", columnDefinition = "tinyint(1) default 0")
    private boolean deleted;
    @CreatedDate
    @Column(name = "createdAt", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @LastModifiedDate
    @Column(name = "updatedAt", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    public Reserves() {
    }

    public Reserves(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Caisses getCaisse() {
        return caisse;
    }

    public void setCaisse(Caisses caisse) {
        this.caisse = caisse;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public Double getMontantDefini() {
        return montantDefini;
    }

    public void setMontantDefini(Double montantDefini) {
        this.montantDefini = montantDefini;
    }

    public Double getMontantSuivant() {
        return montantSuivant;
    }

    public void setMontantSuivant(Double montantSuivant) {
        this.montantSuivant = montantSuivant;
    }

    public Date getDateSuivant() {
        return dateSuivant;
    }

    public void setDateSuivant(Date dateSuivant) {
        this.dateSuivant = dateSuivant;
    }

    public Date getDateReserve() {
        return dateReserve;
    }

    public void setDateReserve(Date dateReserve) {
        this.dateReserve = dateReserve;
    }

    public Integer getJours() {
        return jours;
    }

    public void setJours(Integer jours) {
        this.jours = jours;
    }

    public Double getMontantReserve() {
        return montantReserve;
    }

    public void setMontantReserve(Double montantReserve) {
        this.montantReserve = montantReserve;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
        Reserves reserves = (Reserves) o;
        return deleted == reserves.deleted && Objects.equals(id, reserves.id) && Objects.equals(caisse, reserves.caisse) &&
                Objects.equals(libelle, reserves.libelle) && Objects.equals(jours, reserves.jours) &&
                Objects.equals(heure, reserves.heure) && Objects.equals(montantDefini, reserves.montantDefini) &&
                Objects.equals(montantReserve, reserves.montantReserve) &&
                Objects.equals(montantSuivant, reserves.montantSuivant) && Objects.equals(dateReserve, reserves.dateReserve) &&
                Objects.equals(dateSuivant, reserves.dateSuivant) && Objects.equals(createdAt, reserves.createdAt) &&
                Objects.equals(updatedAt, reserves.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, caisse, libelle, jours, heure, montantDefini, montantReserve, montantSuivant,
                dateReserve, dateSuivant, deleted, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Reserves{" +
                "id=" + id +
                ", caisse=" + caisse +
                ", libelle='" + libelle + '\'' +
                ", jours=" + jours +
                ", heure='" + heure + '\'' +
                ", montantDefini=" + montantDefini +
                ", montantReserve=" + montantReserve +
                ", montantSuivant=" + montantSuivant +
                ", dateReserve=" + dateReserve +
                ", dateSuivant=" + dateSuivant +
                ", deleted=" + deleted +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
