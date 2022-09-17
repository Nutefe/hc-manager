package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.LigneCaisseDao;
import com.bluerizon.hcmanager.models.CaissePK;
import com.bluerizon.hcmanager.models.Caisses;
import com.bluerizon.hcmanager.models.LigneCaisses;
import com.bluerizon.hcmanager.models.Utilisateurs;
import com.bluerizon.hcmanager.repository.LigneCaisseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LigneCaisseService implements LigneCaisseDao {

    @Autowired
    private LigneCaisseRepository repository;

    @Override
    public Optional<LigneCaisses> selectOne(CaissePK caissePK) {
        return repository.findById(caissePK);
    }

    @Override
    public LigneCaisses findFirstByUser(Utilisateurs utilisateur) {
        return repository.findFirstByUser(utilisateur);
    }

    @Override
    public LigneCaisses findFirstByUserCaisse(Utilisateurs utilisateur) {
        return repository.findFirstByUserCaisse(utilisateur);
    }

    @Override
    public LigneCaisses save(LigneCaisses ligneCaisse) {
        return repository.save(ligneCaisse);
    }

    @Override
    public List<LigneCaisses> findByUser(Utilisateurs utilisateur) {
        return repository.findByUser(utilisateur);
    }

    @Override
    public List<LigneCaisses> findByCaisse(Caisses caisse) {
        return repository.findByCaisse(caisse);
    }
}
