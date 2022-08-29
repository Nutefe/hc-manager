package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.PatientsDao;
import com.bluerizon.hcmanager.models.Patients;
import com.bluerizon.hcmanager.repository.PatientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService implements PatientsDao {

    @Autowired
    private PatientsRepository repository;

    @Override
    public Optional<Patients> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Patients save(Patients patient) {
        return repository.save(patient);
    }

    @Override
    public List<Patients> findByDeletedFalse() {
        return repository.findByDeletedFalse();
    }

    @Override
    public List<Patients> findByDeletedFalse(Pageable pageable) {
        return repository.findByDeletedFalse(pageable);
    }

    @Override
    public List<Patients> recherche(String search, Pageable pageable) {
        return repository.recherche(search, pageable);
    }

    @Override
    public Long countPatients() {
        return repository.countPatients();
    }

    @Override
    public Long countRecherche(String search) {
        return repository.countRecherche(search);
    }

    @Override
    public Boolean existsByCodeDossier(String codeDossier) {
        return repository.existsByCodeDossier(codeDossier);
    }

    @Override
    public boolean existsByCodeDossier(String code, Long id) {
        return repository.existsByCodeDossier(code, id);
    }

    @Override
    public List<Patients> selectPatientNonAssurer() {
        return repository.selectPatientNonAssurer();
    }

    @Override
    public List<Patients> selectPatientAssurer() {
        return repository.selectPatientAssurer();
    }

    @Override
    public Long countDate(String date) {
        return repository.countDate(date);
    }
}
