package com.bluerizon.hcmanager.repository;

import com.bluerizon.hcmanager.models.Utilisateurs;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UtilisateursRepository extends JpaRepository<Utilisateurs, Long> {

    Optional<Utilisateurs> findByUsername(String username);

    Optional<Utilisateurs> findByEmail(String email);

    List<Utilisateurs> findByDeletedFalseOrderByIdDesc();


    @Query("SELECT u FROM Utilisateurs u WHERE u.deleted = false AND u.id != :id")
    List<Utilisateurs> selectUser(Long id);

    @Query("SELECT u FROM Utilisateurs u WHERE u.deleted = false AND u.id != :id")
    List<Utilisateurs> selectAllUsers(Long id, Pageable pageable);

    List<Utilisateurs> findByDeletedTrueOrderByIdDesc();

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(email) > 0 THEN true ELSE false END FROM Utilisateurs u WHERE u.email = :email and u.id != :id")
    boolean existsByEmail(@Param("email") final String email, @Param("id") final Long id);

    @Query("SELECT CASE WHEN COUNT(username) > 0 THEN true ELSE false END FROM Utilisateurs u WHERE u.username = :username and u.id != :id")
    boolean existsByUsername(@Param("username") final String username, @Param("id") final Long id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Utilisateurs u set u.password = :password where u.id = :id")
    void updatePassword(@Param("id") final Long id, @Param("password") final String password);

    @Query("SELECT u FROM Utilisateurs u WHERE (u.username LIKE CONCAT('%',:search,'%') OR " +
            "u.nom LIKE CONCAT('%',:search,'%') OR " +
            "u.prenom LIKE CONCAT('%',:search,'%')) AND (u.id != :id And u.deleted = false)")
    List<Utilisateurs> recherche(String search, Long id, Pageable pageable);

    @Query("SELECT COUNT(u) FROM Utilisateurs u WHERE u.deleted = false AND u.id != :id")
    Long countUser(Long id);

    @Query("SELECT COUNT(u) FROM Utilisateurs u WHERE (u.username LIKE CONCAT('%',:search,'%') OR " +
            "u.nom LIKE CONCAT('%',:search,'%') OR " +
            "u.prenom LIKE CONCAT('%',:search,'%')) AND (u.id != :id And u.deleted = false)")
    Long countRecherche(String search, Long id);

}
