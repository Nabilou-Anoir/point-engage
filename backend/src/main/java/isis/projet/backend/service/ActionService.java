package isis.projet.backend.service;

import isis.projet.backend.entity.Action;
import isis.projet.backend.dao.ActionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

import java.util.List;

/**
 * Service pour la gestion des entités Action.
 *
 * Fournit des méthodes CRUD pour manipuler les objets Action.
 */
@Service
@RequiredArgsConstructor
public class ActionService {

    // Injection de la dépendance actionRepository.
    private final ActionRepository actionRepository;

    /**
     * Récupère toutes les Actions de la base de données.
     *
     * @return une liste de toutes les Actions.
     */
    public List<Action> findAll() {
        return actionRepository.findAll();
    }

    /**
     * Recherche une Action par son identifiant.
     *
     * @param id l'identifiant de l'Action.
     * @return l'Action trouvée ou génère une erreur 404.
     */
    public Action findById(Integer id) {
        return actionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Action non trouvée avec id : " + id));
    }

    /**
     * Sauvegarde ou met à jour une Action dans la base de données.
     *
     * @param action l'objet Action à sauvegarder.
     * @return l'Action sauvegardée.
     */
    public Action save(Action action) {
        return actionRepository.save(action);
    }

    /**
     * Met à jour une action existante.
     *
     * @param id l'identifiant de l'Action à mettre à jour.
     * @param updated l'objet contenant les nouvelles valeurs.
     * @return l'Action mise à jour.
     */
    public Action updateAction(Integer id, Action updated) {
        return actionRepository.findById(id)
                .map(action -> {
                    action.setNom(updated.getNom());
                    action.setDescriptionAction(updated.getDescriptionAction());
                    action.setDateDebutAction(updated.getDateDebutAction());
                    action.setDateFinAction(updated.getDateFinAction());
                    action.setReferentiel(updated.getReferentiel());
                    return actionRepository.save(action);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Action non trouvée avec id : " + id));
    }

    /**
     * Supprime une Action de la base de données par son identifiant.
     *
     * @param id l'identifiant de l'Action à supprimer.
     */
    public void deleteById(Integer id) {
        if (!actionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Action non trouvée avec id : " + id);
        }
        actionRepository.deleteById(id);
    }
}