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
@Table(name = "traitements")
@JsonIgnoreProperties(value = {"updatedAt", "deleted", "version" })
@EntityListeners({ AuditingEntityListener.class })
public class Traitements implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "utilisateur", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Utilisateurs utilisateur;
    @JoinColumn(name = "typeTraitement", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private TypeTraitements typeTraitement;
    @JoinColumn(name = "typePatient", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private TypePatients typePatient;
    @Column(name = "libelle", nullable = false, length = 50)
    private String libelle;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private Double price;
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

    public Traitements() {
    }

    public Traitements(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Utilisateurs getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateurs utilisateur) {
        this.utilisateur = utilisateur;
    }

    public TypeTraitements getTypeTraitement() {
        return typeTraitement;
    }

    public void setTypeTraitement(TypeTraitements typeTraitement) {
        this.typeTraitement = typeTraitement;
    }

    public TypePatients getTypePatient() {
        return typePatient;
    }

    public void setTypePatient(TypePatients typePatient) {
        this.typePatient = typePatient;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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
        Traitements that = (Traitements) o;
        return deleted == that.deleted && version == that.version && Objects.equals(id, that.id) &&
                Objects.equals(utilisateur, that.utilisateur) && Objects.equals(typeTraitement, that.typeTraitement) &&
                Objects.equals(typePatient, that.typePatient) && Objects.equals(libelle, that.libelle) &&
                Objects.equals(description, that.description) && Objects.equals(price, that.price) &&
                Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, utilisateur, typeTraitement, typePatient, libelle, description, price, deleted,
                version, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Traitements{" +
                "id=" + id +
                ", utilisateur=" + utilisateur +
                ", typeTraitement=" + typeTraitement +
                ", typePatient=" + typePatient +
                ", libelle='" + libelle + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", deleted=" + deleted +
                ", version=" + version +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
