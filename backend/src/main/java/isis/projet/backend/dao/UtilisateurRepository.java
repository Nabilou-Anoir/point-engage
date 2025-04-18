package isis.projet.backend.dao;

import isis.projet.backend.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    @Query("SELECT u FROM Utilisateur u WHERE u.username = :username")
    Optional<Utilisateur> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM Utilisateur u WHERE u.email = :email")
    Optional<Utilisateur> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM Utilisateur u WHERE u.email = :email AND u.password = :password")
    Optional<Utilisateur> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    @Query("SELECT u FROM Utilisateur u WHERE u.role.name = :roleName")
    List<Utilisateur> findByRoleName(@Param("roleName") String roleName);
}