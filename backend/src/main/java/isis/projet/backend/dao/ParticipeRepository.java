package isis.projet.backend.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import isis.projet.backend.entity.Participe;
import isis.projet.backend.entity.ParticipeKey;

@Repository
public interface ParticipeRepository extends JpaRepository<Participe, ParticipeKey> {

    @Query("SELECT p FROM Participe p WHERE p.id.idEtudiant = :idEtudiant")
    List<Participe> findByIdIdEtudiant(@Param("idEtudiant") Integer idEtudiant);

    @Query("SELECT p FROM Participe p WHERE p.id.idAction = :idAction")
    List<Participe> findByIdIdAction(@Param("idAction") Integer idAction);

    @Query("SELECT p FROM Participe p WHERE p.id.idEtudiant = :idEtudiant AND p.id.idAction = :idAction")
    List<Participe> findByIdIdEtudiantAndId_IdAction(@Param("idEtudiant") Integer idEtudiant,
                                                     @Param("idAction") Integer idAction);

    @Query("SELECT p FROM Participe p WHERE p.id.idAction = :idAction AND p.id.idSemestre = :idSemestre")
    List<Participe> findByIdIdActionAndId_IdSemestre(@Param("idAction") Integer idAction,
                                                     @Param("idSemestre") Integer idSemestre);

    @Query("SELECT p FROM Participe p WHERE p.id.idSemestre = :idSemestre")
    List<Participe> findByIdIdSemestre(@Param("idSemestre") Integer idSemestre);

    @Query("SELECT p FROM Participe p WHERE p.semestre.nbSemestre = :nbSemestre AND FUNCTION('YEAR', p.semestre.anneeUniversitaire) = :year")
    List<Participe> findBySemestreAndYear(@Param("nbSemestre") Integer nbSemestre,
                                          @Param("year") Integer year);
    
    @Query("SELECT p FROM Participe p WHERE p.referent.idReferent = :idReferent")
    List<Participe> findByIdReferent(@Param("idReferent") Integer idReferent);

}