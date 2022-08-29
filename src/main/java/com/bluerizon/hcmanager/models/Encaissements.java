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
@Table(name = "encaissement")
@XmlRootElement
@JsonIgnoreProperties(value = {"updatedAt"})
@EntityListeners(AuditingEntityListener.class)
public class Encaissements implements Serializable {

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
    @JoinColumns({ @JoinColumn(name = "caisse", referencedColumnName = "caisse"),
            @JoinColumn(name = "user", referencedColumnName = "utilisateur") })
    @ManyToOne
    private LigneCaisses ligneCaisse;
    @Basic(optional = false)
    @JoinColumn(name = "facture", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Factures facture;
    @Column(name = "total", nullable = false)
    private Double total;
    @Column(name = "montant", nullable = false)
    private Double montant;
    @Column(name = "reliquat", nullable = false)
    private Double reliquat;
    @Column(name = "reste", nullable = false)
    private Double reste;
    @Column(name = "dateEncaissement")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateEncaissement;
    @Column(name = "fileName")
    private String fileName;
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

    public Encaissements() {
    }

    public Encaissements(Long id) {
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

    public LigneCaisses getLigneCaisse() {
        return ligneCaisse;
    }

    public void setLigneCaisse(LigneCaisses ligneCaisse) {
        this.ligneCaisse = ligneCaisse;
    }

    public Factures getFacture() {
        return facture;
    }

    public void setFacture(Factures facture) {
        this.facture = facture;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Double getReliquat() {
        return reliquat;
    }

    public void setReliquat(Double reliquat) {
        this.reliquat = reliquat;
    }

    public Double getReste() {
        return reste;
    }

    public void setReste(Double reste) {
        this.reste = reste;
    }

    public Date getDateEncaissement() {
        return dateEncaissement;
    }

    public void setDateEncaissement(Date dateEncaissement) {
        this.dateEncaissement = dateEncaissement;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
        Encaissements that = (Encaissements) o;
        return version == that.version && deleted == that.deleted && Objects.equals(id, that.id) &&
                Objects.equals(utilisateur, that.utilisateur) && Objects.equals(ligneCaisse, that.ligneCaisse) &&
                Objects.equals(facture, that.facture) && Objects.equals(total, that.total) &&
                Objects.equals(montant, that.montant) && Objects.equals(reliquat, that.reliquat) &&
                Objects.equals(reste, that.reste) && Objects.equals(dateEncaissement, that.dateEncaissement) &&
                Objects.equals(fileName, that.fileName) && Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, utilisateur, ligneCaisse, facture, total, montant, reliquat, reste, dateEncaissement,
                fileName, version, deleted, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Encaissements{" +
                "id=" + id +
                ", utilisateur=" + utilisateur +
                ", ligneCaisse=" + ligneCaisse +
                ", facture=" + facture +
                ", total=" + total +
                ", montant=" + montant +
                ", reliquat=" + reliquat +
                ", reste=" + reste +
                ", dateEncaissement=" + dateEncaissement +
                ", fileName='" + fileName + '\'' +
                ", version=" + version +
                ", deleted=" + deleted +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
