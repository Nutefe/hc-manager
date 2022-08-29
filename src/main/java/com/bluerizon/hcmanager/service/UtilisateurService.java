package com.bluerizon.hcmanager.service;

import com.bluerizon.hcmanager.dao.UtilisateursDao;
import com.bluerizon.hcmanager.models.Utilisateurs;
import com.bluerizon.hcmanager.repository.UtilisateursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService implements UtilisateursDao {

    @Autowired
    private UtilisateursRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<Utilisateurs> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Utilisateurs> findByUsername(String username) {
        return repository.findByUsername(username);
    }


    @Override
    public List<Utilisateurs> selectAllUsers(Long id, Pageable pageable) {
        return repository.selectAllUsers(id, pageable);
    }

    @Override
    public Optional<Utilisateurs> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public List<Utilisateurs> findByDeletedFalseOrderByIdDesc() {
        return repository.findByDeletedFalseOrderByIdDesc();
    }

    @Override
    public List<Utilisateurs> findByDeletedTrueOrderByIdDesc() {
        return repository.findByDeletedTrueOrderByIdDesc();
    }

    @Override
    public List<Utilisateurs> selectUser(Long id) {
        return repository.selectUser(id);
    }


    @Override
    public Boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Utilisateurs save(Utilisateurs utilisateur) {
        utilisateur.setPassword(this.passwordEncoder.encode(utilisateur.getPassword()));
        return repository.save(utilisateur);
    }

    @Override
    public void update(Utilisateurs utilisateur) {
        repository.save(utilisateur);
    }

    @Override
    public List<Utilisateurs> selectUserCaisse() {
        return repository.selectUserCaisse();
    }

    @Override
    public boolean existsByEmail(String email, Long id) {
        return repository.existsByEmail(email, id);
    }

    @Override
    public boolean existsByUsername(String username, Long id) {
        return repository.existsByUsername(username, id);
    }

    @Override
    public void updatePassword(Long id, String password) {
        repository.updatePassword(id, password);
    }

    @Override
    public List<Utilisateurs> recherche(String search, Long id, Pageable pageable) {
        return repository.recherche(search, id, pageable);
    }

    @Override
    public Long countUser(Long id) {
        return repository.countUser(id);
    }

    @Override
    public Long countRecherche(String search, Long id) {
        return repository.countRecherche(search, id);
    }

}
