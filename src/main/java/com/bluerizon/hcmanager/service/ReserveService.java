package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.ReserveDao;
import com.bluerizon.hcmanager.models.Reserves;
import com.bluerizon.hcmanager.repository.ReservesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ReserveService implements ReserveDao {

    @Autowired
    private ReservesRepository repository;

    @Override
    public Optional<Reserves> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Reserves save(Reserves reserve) {
        return repository.save(reserve);
    }

    @Override
    public List<Reserves> findByDeletedFalse() {
        return repository.findByDeletedFalse();
    }

    @Override
    public List<Reserves> findByDeletedFalse(Pageable pageable) {
        return repository.findByDeletedFalse(pageable);
    }

    @Override
    public List<Reserves> recherche(String search, Pageable pageable) {
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
}
