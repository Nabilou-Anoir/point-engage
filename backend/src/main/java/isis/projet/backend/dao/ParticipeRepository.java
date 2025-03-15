package isis.projet.backend.dao;

import isis.projet.backend.entity.Participe;
import isis.projet.backend.entity.ParticipeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ParticipeRepository extends JpaRepository<Participe, ParticipeKey> {
    List<Participe> findById_IdEtudiant(Integer idEtudiant); // Correction du nom de la méthode

    List<Participe> findById_IdAction(Integer idAction); // Uniformisation avec les autres méthodes

    List<Participe> findById_IdEtudiantAndId_IdAction(Integer idEtudiant, Integer idAction);

    // Récupérer les étudiants qui ont participé à une action lors d'un semestre donné
    List<Participe> findById_IdActionAndId_IdSemestre(Integer idAction, Integer idSemestre);

    // Récupérer les participations par semestre
    List<Participe> findById_IdSemestre(Integer idSemestre);

}