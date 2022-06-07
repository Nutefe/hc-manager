package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.FichesDao;
import com.bluerizon.hcmanager.models.FicheTraitements;
import com.bluerizon.hcmanager.models.Fiches;
import com.bluerizon.hcmanager.models.Patients;
import com.bluerizon.hcmanager.payload.response.FTraitementResponse;
import com.bluerizon.hcmanager.payload.response.FicheResponse;
import com.bluerizon.hcmanager.repository.FicheTraitementRepository;
import com.bluerizon.hcmanager.repository.FichesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FicheService implements FichesDao {

    @Autowired
    private FichesRepository repository;
    @Autowired
    private FicheTraitementRepository traitementRepository;

    @Override
    public Optional<Fiches> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Fiches save(Fiches fiche) {
        return repository.save(fiche);
    }

    @Override
    public List<Fiches> findByPatientAndDeletedFalse(Patients patient, Pageable pageable) {
        return repository.findByPatientAndDeletedFalse(patient, pageable);
    }

    @Override
    public List<Fiches> recherche(Patients patient, String search, Pageable pageable) {
        return repository.recherche(patient, search, pageable);
    }

    @Override
    public List<FicheResponse> findByPatientAndDeletedFalseRes(Patients patient, Pageable pageable) {
        List<Fiches> fiches = repository.findByPatientAndDeletedFalse(patient, pageable);
        List<FicheResponse> responses = new ArrayList<>();
        for (Fiches item :
                fiches) {
            FicheResponse ficheResponse = new FicheResponse();
            List<FicheTraitements> traitements = traitementRepository.findByFiche(item);
            List<FTraitementResponse> list = new ArrayList<>();
            for (FicheTraitements tr:
                    traitements) {
                list.add(new FTraitementResponse(tr.getKota(), tr.isUnite(), tr.getNetPayAssu(), tr.getBaseRembours(), tr.getNetPayBeneficiaire()));
            }
            ficheResponse.setUtilisateur(item.getUtilisateur());
            ficheResponse.setPatient(item.getPatient());
            ficheResponse.setDateFiche(item.getDateFiche());
            ficheResponse.setTraitements(list);
            responses.add(ficheResponse);
        }
        return responses;
    }

    @Override
    public List<FicheResponse> rechercheRes(Patients patient, String search, Pageable pageable) {
        List<Fiches> fiches = repository.recherche(patient, search, pageable);
        List<FicheResponse> responses = new ArrayList<>();
        for (Fiches item :
                fiches) {
            FicheResponse ficheResponse = new FicheResponse();
            List<FicheTraitements> traitements = traitementRepository.findByFiche(item);
            List<FTraitementResponse> list = new ArrayList<>();
            for (FicheTraitements tr:
                    traitements) {
                list.add(new FTraitementResponse(tr.getKota(), tr.isUnite(), tr.getNetPayAssu(), tr.getBaseRembours(), tr.getNetPayBeneficiaire()));
            }
            ficheResponse.setUtilisateur(item.getUtilisateur());
            ficheResponse.setPatient(item.getPatient());
            ficheResponse.setDateFiche(item.getDateFiche());
            ficheResponse.setTraitements(list);
            responses.add(ficheResponse);
        }
        return responses;
    }

    @Override
    public Long countFiches(Patients patient) {
        return repository.countFiches(patient);
    }

    @Override
    public Long countRecherche(Patients patient, String search) {
        return repository.countRecherche(patient, search);
    }
}
