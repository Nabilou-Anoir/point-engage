package isis.projet.backend.controller;

import isis.projet.backend.service.ActionService;
import isis.projet.backend.entity.Action;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur REST pour la gestion des actions.
 *
 * Fournit des endpoints pour effectuer des opérations CRUD sur les actions.
 */
@RestController
@RequestMapping("/api/actions")
@RequiredArgsConstructor
public class ActionController {

    // Injection du service pour gérer la logique métier
    private final ActionService actionService;

    /**
     * Récupère toutes les actions.
     *
     * @return une liste de toutes les actions disponibles.
     */
    @GetMapping
    public List<Action> getAllActions() {
        return actionService.findAll();
    }

    /**
     * Récupère une action spécifique par son identifiant.
     *
     * @param id l'identifiant unique de l'action.
     * @return l'action trouvée, sinon une exception 404 est levée.
     */
    @GetMapping("/{id}")
    public Action findById(@PathVariable Integer id) {
        return actionService.findById(id); //
    }

    /**
     * Crée une nouvelle action.
     *
     * @param action l'objet Action à enregistrer.
     * @return l'action enregistrée.
     */
    @PostMapping
    public Action createAction(@RequestBody Action action) {
        return actionService.save(action);
    }

    /**
     * Met à jour une action existante.
     *
     * @param id l'identifiant unique de l'action à modifier.
     * @param updated l'objet contenant les nouvelles valeurs.
     * @return l'action mise à jour.
     */
    @PutMapping("/{id}")
    public Action updateAction(@PathVariable Integer id, @RequestBody Action updated) {
        return actionService.updateAction(id, updated);
    }

    /**
     * Supprime une action par son identifiant.
     *
     * @param id l'identifiant de l'action à supprimer.
     */
    @DeleteMapping("/{id}")
    public void deleteAction(@PathVariable Integer id) {
        actionService.deleteById(id);
    }
}