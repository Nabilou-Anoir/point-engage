package isis.projet.backend.service;

import isis.projet.backend.entity.Participe;
import isis.projet.backend.entity.ParticipeKey;
import isis.projet.backend.dao.ParticipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service pour la gestion des entités Participe.
 *
 * Cette classe gère les opérations CRUD sur la table d'association "participe",
 * qui utilise une clé composite (id_Etudiant, id_Action, id_Semestre).
 * Elle intègre également une contrainte métier pour vérifier que la valeur de totalPoints ne dépasse pas 0,50.
 */
@Service
@RequiredArgsConstructor
public class ParticipeService {

    private final ParticipeRepository participeRepository;

    /**
     * Récupère toutes les participations de la base de données.
     *
     * @return une liste de toutes les participations.
     */
    public List<Participe> findAll() {
        return participeRepository.findAll();
    }

    /**
     * Recherche une participation par sa clé composite.
     *
     * @param key la clé composite (contenant id_Etudiant, id_Action, id_Semestre).
     * @return la participation si elle existe, sinon lève une exception 404.
     */
    public Participe findById(ParticipeKey key) {
        return participeRepository.findById(key)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participation non trouvée pour la clé fournie"));
    }

    /**
     * Sauvegarde ou met à jour une participation dans la base de données.
     * Avant de persister, vérifie que la valeur de totalPoints ne dépasse pas 0,50.
     *
     * @param participe l'objet Participe à sauvegarder.
     * @return la participation sauvegardée.
     * @throws IllegalArgumentException si totalPoints dépasse 0,50.
     */
    public Participe save(Participe participe) {
        validateTotalPoints(participe);
        return participeRepository.save(participe);
    }

    /**
     * Met à jour une participation existante.
     *
     * @param key la clé composite de la participation.
     * @param updated l'objet contenant les nouvelles valeurs.
     * @return la participation mise à jour.
     */
    public Participe updateParticipe(ParticipeKey key, Participe updated) {
        Participe participe = findById(key);
        participe.setTotalPoints(updated.getTotalPoints());
        participe.setNbParticipation(updated.getNbParticipation());
        participe.setDateDebutParticipation(updated.getDateDebutParticipation());
        participe.setDateFinParticipation(updated.getDateFinParticipation());
        participe.setDescriptionParticipation(updated.getDescriptionParticipation());
        participe.setStatut(updated.getStatut());
        validateTotalPoints(participe);
        return participeRepository.save(participe);
    }

    /**
     * Supprime une participation de la base de données à partir de sa clé composite.
     *
     * @param key la clé composite de la participation.
     */
    @Transactional
    public void deleteById(ParticipeKey key) {
        if (!participeRepository.existsById(key)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participation non trouvée pour la clé fournie");
        }
        participeRepository.deleteById(key);
    }

    /**
     * Vérifie que la valeur de totalPoints ne dépasse pas 0,50.
     *
     * @param participe la participation à valider.
     * @throws IllegalArgumentException si totalPoints est supérieur à 0,50.
     */
    private void validateTotalPoints(Participe participe) {
        if (participe.getTotalPoints() != null &&
                participe.getTotalPoints().compareTo(new BigDecimal("0.50")) > 0) {
            throw new IllegalArgumentException("totalPoints ne doit pas dépasser 0,50");
        }
    }

    public List<Participe> getParticipationsByAction(Integer idAction) {
        return participeRepository.findById_IdAction(idAction);
    }
    public List<Participe> getParticipationsByEtudiant(Integer idEtudiant) {
        return participeRepository.findById_IdEtudiant(idEtudiant);
    }
    public List<Participe> getParticipationsByEtudiantAndAction(Integer idEtudiant, Integer idAction) {
        return participeRepository.findById_IdEtudiantAndId_IdAction(idEtudiant, idAction);
    }
    public List<Participe> getEtudiantsByActionAndSemestre(Integer idAction, Integer idSemestre) {
        return participeRepository.findById_IdActionAndId_IdSemestre(idAction, idSemestre);
    }
    public List<Participe> getParticipationsBySemestre(Integer idSemestre) {
        return participeRepository.findById_IdSemestre(idSemestre);
    }
}