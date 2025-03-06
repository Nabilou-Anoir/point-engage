package isis.projet.backend.controller;

import isis.projet.backend.dao.ActionRepository;
import isis.projet.backend.entity.Action;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur pour gérer les actions.
 */
@RestController
@RequestMapping("/api/actions")
@RequiredArgsConstructor
public class ActionController {

    private final ActionRepository actionRepository;

    /**
     * Récupère toutes les actions.
     *
     * @return une liste d'actions.
     */
    @GetMapping
    public List<Action> getAllActions() {
        return actionRepository.findAll();
    }

    /**
     * Crée une nouvelle action.
     *
     * @param action l'action à créer (incluant la date).
     * @return l'action créée.
     */
    @PostMapping
    public Action createAction(@RequestBody Action action) {
        return actionRepository.save(action);
    }

    /**
     * Récupère une action par son identifiant.
     *
     * @param id l'identifiant de l'action.
     * @return l'action trouvée ou null si non trouvée.
     */
    @GetMapping("/{id}")
    public Action getActionById(@PathVariable Integer id) {
        return actionRepository.findById(Long.valueOf(id)).orElse(null);
    }

    /**
     * Met à jour une action existante.
     *
     * @param id      l'identifiant de l'action à mettre à jour.
     * @param updated l'objet Action contenant les nouvelles valeurs (y compris la date).
     * @return l'action mise à jour ou null si l'action n'existe pas.
     */
    @PutMapping("/{id}")
    public Action updateAction(@PathVariable Integer id, @RequestBody Action updated) {
        return actionRepository.findById(Long.valueOf(id))
                .map(a -> {
                    a.setDescriptionAction(updated.getDescriptionAction());
                    a.setDateDebutAction(updated.getDateDebutAction());
                    a.setDateFinAction(updated.getDateFinAction());
                    a.setReferent(updated.getReferent());
                    a.setReferentiel(updated.getReferentiel());
                    a.setNom(updated.getNom());
                    return actionRepository.save(a);
                })
                .orElse(null);
    }

    /**
     * Supprime une action par son identifiant.
     *
     * @param id l'identifiant de l'action à supprimer.
     */
    @DeleteMapping("/{id}")
    public void deleteAction(@PathVariable Integer id) {
        actionRepository.deleteById(Long.valueOf(id));
    }
}