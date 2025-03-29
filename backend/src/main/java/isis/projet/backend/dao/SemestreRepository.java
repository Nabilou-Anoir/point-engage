package isis.projet.backend.dao;
import isis.projet.backend.entity.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface SemestreRepository extends JpaRepository<Semestre, Integer> {

    @Query("SELECT s FROM Semestre s WHERE s.anneeUniversitaire = :annee")
    Semestre findByAnnee(Date annee);

}