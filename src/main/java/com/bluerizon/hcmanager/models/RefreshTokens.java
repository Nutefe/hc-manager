// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "refreshtokens")
@JsonIgnoreProperties(value = {"id"}, allowGetters = true)
@EntityListeners({ AuditingEntityListener.class })
public class RefreshTokens implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    @JoinColumn(name = "utilisateur", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Utilisateurs utilisateur;
    @Column(nullable = false, unique = true)
    private String token;
    @Column(nullable = false)
    private Instant expiryDate;
    
    public RefreshTokens() {
    }

    public RefreshTokens(long id, Utilisateurs utilisateur, String token, Instant expiryDate) {
        this.id = id;
        this.utilisateur = utilisateur;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public long getId() {
        return this.id;
    }
    
    public void setId(final long id) {
        this.id = id;
    }

    public Utilisateurs getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateurs utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getToken() {
        return this.token;
    }
    
    public void setToken(final String token) {
        this.token = token;
    }
    
    public Instant getExpiryDate() {
        return this.expiryDate;
    }
    
    public void setExpiryDate(final Instant expiryDate) {
        this.expiryDate = expiryDate;
    }
}
