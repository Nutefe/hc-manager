package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.ReserveDao;
import com.bluerizon.hcmanager.models.Caisses;
import com.bluerizon.hcmanager.models.Reserves;
import com.bluerizon.hcmanager.repository.ReservesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    public Reserves findByCaisseAndDeletedFalse(Caisses caisse) {
        return repository.findByCaisseAndDeletedFalse(caisse);
    }

    @Override
    public Reserves findTop1ByDeletedFalse() {
        return repository.findTop1ByDeletedFalseOrderByIdDesc();
    }

    @Override
    public Reserves findFirst1ByDeletedFalseOrderByIdDesc() {
        return repository.findFirst1ByDeletedFalseOrderByIdAsc();
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
    public List<Reserves> selectAllReserves() {
        return repository.selectAllReserves();
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

    @Override
    public Double montantDateReserves(Date dateReserve) {
        return repository.montantDateReserves(dateReserve);
    }

    @Override
    public Double montantTotalReserves() {
        return repository.montantTotalReserves();
    }

    @Override
    public Double montantDateReserve(Date dateStart, Date dateEnd) {
        return repository.montantDateReserve(dateStart, dateEnd);
    }
}
