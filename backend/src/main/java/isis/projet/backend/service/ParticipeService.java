package isis.projet.backend.service;

import isis.projet.backend.entity.Participe;
import isis.projet.backend.entity.ParticipeKey;
import isis.projet.backend.entity.Etudiant;
import isis.projet.backend.entity.Action;
import isis.projet.backend.entity.Semestre;
import isis.projet.backend.dao.ParticipeRepository;
import isis.projet.backend.dao.EtudiantRepository;
import isis.projet.backend.dao.ActionRepository;
import isis.projet.backend.dao.SemestreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipeService {

    private final ParticipeRepository participeRepository;
    private final EtudiantRepository etudiantRepository;
    private final ActionRepository actionRepository;
    private final SemestreRepository semestreRepository;

    public List<Participe> findAll() {
        return participeRepository.findAll();
    }

    public Participe findById(ParticipeKey key) {
        return participeRepository.findById(key)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participation non trouvée pour la clé fournie"));
    }

    /**
     * Avant de sauvegarder, nous récupérons les entités existantes afin que JPA
     * gère correctement les associations.
     */
    public Participe save(Participe participe) {
        // Récupération des entités associées
        Integer idEtudiant = participe.getEtudiant().getIdEtudiant();
        Integer idAction = participe.getAction().getIdAction();
        Integer idSemestre = participe.getSemestre().getIdSemestre();

        Etudiant etudiant = etudiantRepository.findById(idEtudiant)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Étudiant non trouvé avec id : " + idEtudiant));
        Action action = actionRepository.findById(idAction)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Action non trouvée avec id : " + idAction));
        Semestre semestre = semestreRepository.findById(idSemestre)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Semestre non trouvé avec id : " + idSemestre));

        // Affectation des entités gérées
        participe.setEtudiant(etudiant);
        participe.setAction(action);
        participe.setSemestre(semestre);

        // Mise à jour de la clé composite
        ParticipeKey key = new ParticipeKey(idEtudiant, idAction, idSemestre);
        participe.setId(key);

        validateTotalPoints(participe);
        return participeRepository.save(participe);
    }

    public Participe updateParticipe(ParticipeKey key, Participe updated) {
        Participe participe = findById(key);
        participe.setTotalPoints(updated.getTotalPoints());
        participe.setNbParticipation(updated.getNbParticipation());
        participe.setDateDebutParticipation(updated.getDateDebutParticipation());
        participe.setDateFinParticipation(updated.getDateFinParticipation());
        participe.setDescriptionParticipation(updated.getDescriptionParticipation());
        participe.setStatut(updated.getStatut());
        participe.setReferent(updated.getReferent());
        participe.setResumeDirecteur(updated.getResumeDirecteur());
        participe.setRemarqueReferent(updated.getRemarqueReferent());
        participe.setPointAction(updated.getPointAction());

        validateTotalPoints(participe);
        return participeRepository.save(participe);
    }

    @Transactional
    public void deleteById(ParticipeKey key) {
        if (!participeRepository.existsById(key)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participation non trouvée pour la clé fournie");
        }
        participeRepository.deleteById(key);
    }

    private void validateTotalPoints(Participe participe) {
        if (participe.getTotalPoints() != null &&
                participe.getTotalPoints().compareTo(new BigDecimal("0.50")) > 0) {
            throw new IllegalArgumentException("totalPoints ne doit pas dépasser 0,50");
        }
    }

    public List<Participe> getParticipationsByAction(Integer idAction) {
        return participeRepository.findByIdIdAction(idAction);
    }
    public List<Participe> getParticipationsByEtudiant(Integer idEtudiant) {
        return participeRepository.findByIdIdEtudiant(idEtudiant);
    }
    public List<Participe> getParticipationsByEtudiantAndAction(Integer idEtudiant, Integer idAction) {
        return participeRepository.findByIdIdEtudiantAndId_IdAction(idEtudiant, idAction);
    }
    public List<Participe> getEtudiantsByActionAndSemestre(Integer idAction, Integer idSemestre) {
        return participeRepository.findByIdIdActionAndId_IdSemestre(idAction, idSemestre);
    }
    public List<Participe> getParticipationsBySemestre(Integer idSemestre) {
        return participeRepository.findByIdIdSemestre(idSemestre);
    }
}