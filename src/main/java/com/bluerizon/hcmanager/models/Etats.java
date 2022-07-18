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
@Table(name = "etats")
@XmlRootElement
@JsonIgnoreProperties(value = {"updatedAt", "created"})
@EntityListeners(AuditingEntityListener.class)
public class Etats implements Serializable {
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
    @JoinColumn(name = "assurance", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Assurances assurance;
    @Column(name = "fileName")
    private String fileName;
    @Column(name = "type")
    private String type;
    @Column(name = "dateEtat", nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateEtat;
    @Column(name = "dateDebut", nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateDebut;
    @Column(name = "dateFin", nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateFin;
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

    public Etats() {
    }

    public Etats(Long id) {
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

    public Assurances getAssurance() {
        return assurance;
    }

    public void setAssurance(Assurances assurance) {
        this.assurance = assurance;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateEtat() {
        return dateEtat;
    }

    public void setDateEtat(Date dateEtat) {
        this.dateEtat = dateEtat;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
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
        Etats etats = (Etats) o;
        return version == etats.version && deleted == etats.deleted && Objects.equals(id, etats.id) &&
                Objects.equals(utilisateur, etats.utilisateur) && Objects.equals(assurance, etats.assurance) &&
                Objects.equals(fileName, etats.fileName) && Objects.equals(type, etats.type) &&
                Objects.equals(dateEtat, etats.dateEtat) && Objects.equals(dateDebut, etats.dateDebut) &&
                Objects.equals(dateFin, etats.dateFin) && Objects.equals(createdAt, etats.createdAt) &&
                Objects.equals(updatedAt, etats.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, utilisateur, assurance, fileName, type, dateEtat, dateDebut, dateFin, version,
                deleted, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Etats{" +
                "id=" + id +
                ", utilisateur=" + utilisateur +
                ", assurance=" + assurance +
                ", fileName='" + fileName + '\'' +
                ", type='" + type + '\'' +
                ", dateEtat=" + dateEtat +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", version=" + version +
                ", deleted=" + deleted +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
