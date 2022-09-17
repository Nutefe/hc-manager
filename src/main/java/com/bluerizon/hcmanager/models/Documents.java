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
@Table(name = "documents")
@JsonIgnoreProperties(value = {"updatedAt",  "deleted"})
@EntityListeners({ AuditingEntityListener.class })
public class Documents implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "libelle", length = 50)
    private String libelle;
    @Column(name = "url", length = 50)
    private String url;
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

    public Documents() {
    }

    public Documents(Integer id) {
        this.id = id;
    }

    public Documents(String libelle, String url) {
        this.libelle = libelle;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        Documents documents = (Documents) o;
        return deleted == documents.deleted && Objects.equals(id, documents.id) && Objects.equals(libelle, documents.libelle) &&
                Objects.equals(url, documents.url) && Objects.equals(createdAt, documents.createdAt) &&
                Objects.equals(updatedAt, documents.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libelle, url, deleted, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Documents{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", url='" + url + '\'' +
                ", deleted=" + deleted +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
