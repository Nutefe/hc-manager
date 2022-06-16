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
@Table(name = "patients")
@JsonIgnoreProperties(value = {"updatedAt", "deleted", "version" })
@EntityListeners({ AuditingEntityListener.class })
public class Patients implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "codeDossier", unique = true, length = 50, nullable = false)
    private String codeDossier;
    @JoinColumn(name = "utilisateur", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Utilisateurs utilisateur;
    @JoinColumn(name = "typePatient", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private TypePatients typePatient;
    @JoinColumn(name = "assurance", referencedColumnName = "id")
    @ManyToOne
    private Assurances assurance;
    @JoinColumn(name = "entreprise", referencedColumnName = "id")
    @ManyToOne
    private Entreprises entreprise;
    @Column(name = "nom", nullable = false, length = 50)
    private String nom;
    @Column(name = "prenom", nullable = false, length = 80)
    private String prenom;
    @Column(name = "dateNaiss")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateNaiss;
    @Column(name = "genre", nullable = false)
    private String genre;
    @Column(name = "telephone", nullable = false, length = 50)
    private String telephone;
    @Column(name = "adresse", nullable = false, length = 150)
    private String adresse;
    @Column(name = "numeroPiece", length = 50)
    private String numeroPiece;
    @Column(name = "pieceExp")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date pieceExp;
    @Column(name = "deleted")
    private boolean deleted;
    @Version
    @Basic(optional = false)
    @Column(nullable = false)
    private int version;
    @CreatedDate
    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    public Patients() {
    }

    public Patients(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeDossier() {
        return codeDossier;
    }

    public void setCodeDossier(String codeDossier) {
        this.codeDossier = codeDossier;
    }

    public Assurances getAssurance() {
        return assurance;
    }

    public void setAssurance(Assurances assurance) {
        this.assurance = assurance;
    }

    public Utilisateurs getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateurs utilisateur) {
        this.utilisateur = utilisateur;
    }

    public TypePatients getTypePatient() {
        return typePatient;
    }

    public void setTypePatient(TypePatients typePatient) {
        this.typePatient = typePatient;
    }

    public Entreprises getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprises entreprise) {
        this.entreprise = entreprise;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Date getDateNaiss() {
        return dateNaiss;
    }

    public void setDateNaiss(Date dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNumeroPiece() {
        return numeroPiece;
    }

    public void setNumeroPiece(String numeroPiece) {
        this.numeroPiece = numeroPiece;
    }

    public Date getPieceExp() {
        return pieceExp;
    }

    public void setPieceExp(Date pieceExp) {
        this.pieceExp = pieceExp;
    }

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
        Patients patients = (Patients) o;
        return deleted == patients.deleted && version == patients.version && Objects.equals(id, patients.id) &&
                Objects.equals(codeDossier, patients.codeDossier) && Objects.equals(utilisateur, patients.utilisateur) &&
                Objects.equals(typePatient, patients.typePatient) && Objects.equals(assurance, patients.assurance) &&
                Objects.equals(entreprise, patients.entreprise) && Objects.equals(nom, patients.nom) &&
                Objects.equals(prenom, patients.prenom) && Objects.equals(dateNaiss, patients.dateNaiss) &&
                Objects.equals(genre, patients.genre) && Objects.equals(telephone, patients.telephone) &&
                Objects.equals(adresse, patients.adresse) && Objects.equals(numeroPiece, patients.numeroPiece) &&
                Objects.equals(pieceExp, patients.pieceExp) && Objects.equals(createdAt, patients.createdAt) &&
                Objects.equals(updatedAt, patients.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codeDossier, utilisateur, typePatient, assurance, entreprise, nom, prenom, dateNaiss,
                genre, telephone, adresse, numeroPiece, pieceExp, deleted, version, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Patients{" +
                "id=" + id +
                ", codeDossier='" + codeDossier + '\'' +
                ", utilisateur=" + utilisateur +
                ", typePatient=" + typePatient +
                ", assurance=" + assurance +
                ", entreprise=" + entreprise +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateNaiss=" + dateNaiss +
                ", genre=" + genre +
                ", telephone='" + telephone + '\'' +
                ", adresse='" + adresse + '\'' +
                ", numeroPiece='" + numeroPiece + '\'' +
                ", pieceExp=" + pieceExp +
                ", deleted=" + deleted +
                ", version=" + version +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
