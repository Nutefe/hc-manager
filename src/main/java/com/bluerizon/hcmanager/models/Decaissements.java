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
@Table(name = "decaissements")
@JsonIgnoreProperties(value = {"updatedAt",  "deleted"})
@EntityListeners({ AuditingEntityListener.class })
public class Decaissements implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumns({ @JoinColumn(name = "caisse", referencedColumnName = "caisse"),
            @JoinColumn(name = "utilisateur", referencedColumnName = "utilisateur") })
    @ManyToOne
    private LigneCaisses ligneCaisse;
    @Column(name = "type", length = 50)
    private String type;
    @Column(name = "motif", length = 250)
    private String motif;
    @Column(name = "montant")
    private Double montant;
    @Column(name = "dateDecaissement")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateDecaissement;
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

    public Decaissements() {
    }

    public Decaissements(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LigneCaisses getLigneCaisse() {
        return ligneCaisse;
    }

    public void setLigneCaisse(LigneCaisses ligneCaisse) {
        this.ligneCaisse = ligneCaisse;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Date getDateDecaissement() {
        return dateDecaissement;
    }

    public void setDateDecaissement(Date dateDecaissement) {
        this.dateDecaissement = dateDecaissement;
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
        Decaissements that = (Decaissements) o;
        return deleted == that.deleted && Objects.equals(id, that.id) && Objects.equals(ligneCaisse, that.ligneCaisse) && Objects.equals(type, that.type) && Objects.equals(motif, that.motif) && Objects.equals(montant, that.montant) && Objects.equals(dateDecaissement, that.dateDecaissement) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ligneCaisse, type, motif, montant, dateDecaissement, deleted, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Decaissements{" +
                "id=" + id +
                ", ligneCaisse=" + ligneCaisse +
                ", type='" + type + '\'' +
                ", motif='" + motif + '\'' +
                ", montant=" + montant +
                ", dateDecaissement=" + dateDecaissement +
                ", deleted=" + deleted +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
