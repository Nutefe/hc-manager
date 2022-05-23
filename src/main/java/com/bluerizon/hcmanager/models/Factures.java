/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluerizon.hcmanager.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author licorne
 */
@Entity
@Table(name = "factures")
@XmlRootElement
@JsonIgnoreProperties(value = {"updatedAt", "created"})
@EntityListeners(AuditingEntityListener.class)
public class Factures implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @JoinColumn(name = "utilisateur", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Utilisateurs utilisateur;
    @Basic(optional = false)
    @JoinColumn(name = "fiche", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Fiches fiche;
    @Column(name = "solde", columnDefinition="tinyint(1) default 0")
    private boolean solde;
    @Column(name = "encaisse", columnDefinition="tinyint(1) default 0")
    private boolean encaisse;
    @Column(name = "fileName")
    private String fileName;
    @Column(name = "acompte", columnDefinition="int default 0.0")
    private Double acompte;
    @Column(name = "remise", columnDefinition="int default 0.0")
    private Double remise;
    @Column(name = "reste", columnDefinition="int default 0.0")
    private Double reste;
    @Column(name = "dateFacture", nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateFacture;
    @Version
    @Basic(optional = false)
    @Column(nullable = false)
    private int version;
    @Column(name = "deleted", columnDefinition="tinyint(1) default 0")
    private boolean deleted;
    @CreatedDate
    @Column(name = "createdAt", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @LastModifiedDate
    @Column(name = "updatedAt", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public Factures() {
    }

    public Factures(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utilisateurs getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateurs utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Fiches getFiche() {
        return fiche;
    }

    public void setFiche(Fiches fiche) {
        this.fiche = fiche;
    }

    public boolean isSolde() {
        return solde;
    }

    public void setSolde(boolean solde) {
        this.solde = solde;
    }

    public boolean isEncaisse() {
        return encaisse;
    }

    public void setEncaisse(boolean encaisse) {
        this.encaisse = encaisse;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Double getAcompte() {
        return acompte;
    }

    public void setAcompte(Double acompte) {
        this.acompte = acompte;
    }

    public Double getRemise() {
        return remise;
    }

    public void setRemise(Double remise) {
        this.remise = remise;
    }

    public Double getReste() {
        return reste;
    }

    public void setReste(Double reste) {
        this.reste = reste;
    }

    public Date getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(Date dateFacture) {
        this.dateFacture = dateFacture;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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
        Factures factures = (Factures) o;
        return solde == factures.solde && encaisse == factures.encaisse && version == factures.version &&
                deleted == factures.deleted && Objects.equals(id, factures.id) &&
                Objects.equals(utilisateur, factures.utilisateur) && Objects.equals(fiche, factures.fiche) &&
                Objects.equals(fileName, factures.fileName) && Objects.equals(acompte, factures.acompte) &&
                Objects.equals(remise, factures.remise) && Objects.equals(reste, factures.reste) &&
                Objects.equals(dateFacture, factures.dateFacture) && Objects.equals(createdAt, factures.createdAt) &&
                Objects.equals(updatedAt, factures.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, utilisateur, fiche, solde, encaisse, fileName, acompte, remise, reste, dateFacture,
                version, deleted, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Factures{" +
                "id=" + id +
                ", utilisateur=" + utilisateur +
                ", fiche=" + fiche +
                ", solde=" + solde +
                ", encaisse=" + encaisse +
                ", fileName='" + fileName + '\'' +
                ", acompte=" + acompte +
                ", remise=" + remise +
                ", reste=" + reste +
                ", dateFacture=" + dateFacture +
                ", version=" + version +
                ", deleted=" + deleted +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
