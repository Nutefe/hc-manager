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
@Table(name = "ficheTraitements")
@XmlRootElement
@EntityListeners({ AuditingEntityListener.class })
public class FicheTraitements implements Serializable
{
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FicheTraitementPK ficheTraitementPK;
    @JoinColumn(name = "kota", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Kotas kota;
    @Column(name = "taux", nullable = false)
    private Double taux;
    @Column(name = "unite", nullable = false)
    private boolean unite;
    @Column(name = "netPayAssu")
    private Double netPayAssu;
    @Column(name = "baseRembours")
    private Double baseRembours;
    @Column(name = "netPayBeneficiaire")
    private Double netPayBeneficiaire;
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

    public FicheTraitements() {
        this.deleted = false;
    }

    public FicheTraitements(FicheTraitementPK ficheTraitementPK) {
        this.ficheTraitementPK = ficheTraitementPK;
    }

    public FicheTraitementPK getFicheTraitementPK() {
        return ficheTraitementPK;
    }

    public void setFicheTraitementPK(FicheTraitementPK ficheTraitementPK) {
        this.ficheTraitementPK = ficheTraitementPK;
    }

    public Kotas getKota() {
        return kota;
    }

    public void setKota(Kotas kota) {
        this.kota = kota;
    }

    public Double getTaux() {
        return taux;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public boolean isUnite() {
        return unite;
    }

    public void setUnite(boolean unite) {
        this.unite = unite;
    }

    public Double getNetPayAssu() {
        return netPayAssu;
    }

    public void setNetPayAssu(Double netPayAssu) {
        this.netPayAssu = netPayAssu;
    }

    public Double getBaseRembours() {
        return baseRembours;
    }

    public void setBaseRembours(Double baseRembours) {
        this.baseRembours = baseRembours;
    }

    public Double getNetPayBeneficiaire() {
        return netPayBeneficiaire;
    }

    public void setNetPayBeneficiaire(Double netPayBeneficiaire) {
        this.netPayBeneficiaire = netPayBeneficiaire;
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
        FicheTraitements that = (FicheTraitements) o;
        return unite == that.unite && deleted == that.deleted && version == that.version && Objects.equals(ficheTraitementPK, that.ficheTraitementPK) && Objects.equals(kota, that.kota) && Objects.equals(taux, that.taux) && Objects.equals(netPayAssu, that.netPayAssu) && Objects.equals(baseRembours, that.baseRembours) && Objects.equals(netPayBeneficiaire, that.netPayBeneficiaire) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ficheTraitementPK, kota, taux, unite, netPayAssu, baseRembours, netPayBeneficiaire, deleted, version, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "FicheTraitements{" +
                "ficheTraitementPK=" + ficheTraitementPK +
                ", kota=" + kota +
                ", taux=" + taux +
                ", unite=" + unite +
                ", netPayAssu=" + netPayAssu +
                ", baseRembours=" + baseRembours +
                ", netPayBeneficiaire=" + netPayBeneficiaire +
                ", deleted=" + deleted +
                ", version=" + version +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
