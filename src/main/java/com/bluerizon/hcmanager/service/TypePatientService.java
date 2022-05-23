package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.TypePatientsDao;
import com.bluerizon.hcmanager.models.TypePatients;
import com.bluerizon.hcmanager.repository.TypePatientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypePatientService implements TypePatientsDao {

    @Autowired
    private TypePatientsRepository repository;

    @Override
    public Optional<TypePatients> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public TypePatients save(TypePatients typePatient) {
        return repository.save(typePatient);
    }

    @Override
    public List<TypePatients> findByDeletedFalse() {
        return repository.findByDeletedFalse();
    }

    @Override
    public List<TypePatients> findByDeletedFalse(Pageable pageable) {
        return repository.findByDeletedFalse(pageable);
    }

    @Override
    public List<TypePatients> recherche(String search, Pageable pageable) {
        return repository.recherche(search, pageable);
    }

    @Override
    public Long countTypePatients() {
        return repository.countTypePatients();
    }

    @Override
    public Long countRecherche(String search) {
        return repository.countRecherche(search);
    }
}
