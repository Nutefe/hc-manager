package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.FicheTraitementsDao;
import com.bluerizon.hcmanager.models.FicheTraitements;
import com.bluerizon.hcmanager.models.Fiches;
import com.bluerizon.hcmanager.repository.FicheTraitementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FicheTraitementService implements FicheTraitementsDao {

    @Autowired
    private FicheTraitementRepository repository;

    @Override
    public FicheTraitements save(FicheTraitements ficheTraitement) {
        return repository.save(ficheTraitement);
    }

    @Override
    public List<FicheTraitements> saveAll(List<FicheTraitements> ficheTraitements) {
        return repository.saveAll(ficheTraitements);
    }

    @Override
    public List<FicheTraitements> findByFiche(Fiches fiche) {
        return repository.findByFiche(fiche);
    }

    @Override
    public void delete(List<FicheTraitements> ficheTraitements) {
        repository.deleteAll(ficheTraitements);
    }
}
