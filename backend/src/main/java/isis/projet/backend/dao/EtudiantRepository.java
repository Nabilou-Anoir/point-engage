package isis.projet.backend.dao;

import isis.projet.backend.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Integer> {

    @Query("SELECT e FROM Etudiant e WHERE e.email = :email")
    Etudiant findByEmail(@Param("email") String email);

}