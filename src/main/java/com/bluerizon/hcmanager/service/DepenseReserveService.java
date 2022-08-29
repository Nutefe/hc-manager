package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.DepenseReserveDao;
import com.bluerizon.hcmanager.models.DepenseReserves;
import com.bluerizon.hcmanager.repository.DepenseReservesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DepenseReserveService implements DepenseReserveDao {

    @Autowired
    private DepenseReservesRepository repository;

    @Override
    public Optional<DepenseReserves> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public DepenseReserves save(DepenseReserves depenseReserve) {
        return repository.save(depenseReserve);
    }

    @Override
    public List<DepenseReserves> findByDeletedFalse() {
        return repository.findByDeletedFalse();
    }

    @Override
    public List<DepenseReserves> findByDeletedFalse(Pageable pageable) {
        return repository.findByDeletedFalse(pageable);
    }

    @Override
    public List<DepenseReserves> recherche(String search, Pageable pageable) {
        return repository.recherche(search, pageable);
    }

    @Override
    public Long countReserves() {
        return repository.countReserves();
    }

    @Override
    public Long countRecherche(String search) {
        return repository.countRecherche(search);
    }

    @Override
    public Double montantReserves(Date dateDepense) {
        return repository.montantReserves(dateDepense);
    }

    @Override
    public Double montantTotalReserves() {
        return repository.montantTotalReserves();
    }
}
