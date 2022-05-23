package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.ProfilsDao;
import com.bluerizon.hcmanager.models.Profils;
import com.bluerizon.hcmanager.repository.ProfilsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfilService implements ProfilsDao {

    @Autowired
    private ProfilsRepository repository;

    @Override
    public Optional<Profils> findByLibelle(String libelle) {
        return repository.findByLibelle(libelle);
    }

    @Override
    public Optional<Profils> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public List<Profils> findByDeletedFalse() {
        return repository.selectProfils();
    }

    @Override
    public List<Profils> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Profils> selectProfils(Pageable pageable) {
        return repository.selectProfils(pageable);
    }

    @Override
    public List<Profils> recherche(String search, Pageable pageable) {
        return repository.recherche(search, pageable);
    }

    @Override
    public Long count() {
        return repository.count();
    }

    @Override
    public Long countProfils() {
        return repository.countProfils();
    }

    @Override
    public Long countRecherche(String search) {
        return repository.countRecherche(search);
    }

    @Override
    public Profils save(Profils profil) {
        return repository.save(profil);
    }

}
