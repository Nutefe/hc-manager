package com.bluerizon.hcmanager.dao;

import com.bluerizon.hcmanager.models.Utilisateurs;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UtilisateursDao {

    Optional<Utilisateurs> findById(Long id);
    Optional<Utilisateurs> findByUsername(String username);

    Optional<Utilisateurs> findByEmail(String email);

    List<Utilisateurs> findByDeletedFalseOrderByIdDesc();

    List<Utilisateurs> selectUser(Long id);

    List<Utilisateurs> selectAllUsers(Long id, Pageable pageable);

    List<Utilisateurs> findByDeletedTrueOrderByIdDesc();

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    boolean existsByEmail(@Param("email") final String email, @Param("id") final Long id);

    boolean existsByUsername(@Param("username") final String username, @Param("id") final Long id);

    void updatePassword(@Param("id") final Long id, @Param("password") final String password);

    List<Utilisateurs> recherche(String search, Long id, Pageable pageable);

    Long countUser(Long id);

    Long countRecherche(String search, Long id);

    Utilisateurs save(Utilisateurs utilisateur);

    void update(Utilisateurs utilisateur);


}
