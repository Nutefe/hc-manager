package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.Profils;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProfilsDao {

    Optional<Profils> findByLibelle(String libelle);
    Optional<Profils> findById(Integer id);
    List<Profils> findByDeletedFalse();
    List<Profils> findAll();
    List<Profils> selectProfils(Pageable pageable);
    List<Profils> recherche(String search, Pageable pageable);
    Long count();
    Long countProfils();
    Long countRecherche(String search);
    Profils save(Profils profil);

}
