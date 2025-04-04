package isis.projet.backend.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isis.projet.backend.dao.ActionRepository;
import isis.projet.backend.dao.ReferentRepository;
import isis.projet.backend.entity.Action;
import isis.projet.backend.entity.Referent;
import isis.projet.backend.service.ActionService;
import lombok.RequiredArgsConstructor;

/**
 * Contrôleur REST pour la gestion des actions.
 * Fournit des endpoints pour effectuer des opérations CRUD sur les actions.
 */
@RestController
@RequestMapping("/api/actions")
@RequiredArgsConstructor
public class ActionController {

    // Service métier principal
    private final ActionService actionService;

    @Autowired
    private ActionRepository actionRepository;

    @Autowired
    private ReferentRepository referentRepository;

    /**
     * Récupère toutes les actions.
     * @return une liste de toutes les actions disponibles.
     */
    @GetMapping
    public List<Action> getAllActions() {
        return actionService.findAll();
    }

    /**
     * Récupère une action spécifique par son identifiant.
     * @param id l'identifiant unique de l'action.
     * @return l'action trouvée.
     */
    @GetMapping("/{id}")
    public Action findById(@PathVariable Integer id) {
        return actionService.findById(id);
    }

    /**
     * Crée une nouvelle action.
     * @param action l'objet Action à enregistrer.
     * @return l'action enregistrée.
     */
    @PostMapping
    public Action createAction(@RequestBody Action action) {
        return actionService.save(action);
    }

    /**
     * Met à jour une action existante.
     * @param id l'identifiant de l'action à modifier.
     * @param updated l'objet contenant les nouvelles valeurs.
     * @return l'action mise à jour.
     */
    @PutMapping("/{id}")
    public Action updateAction(@PathVariable Integer id, @RequestBody Action updated) {
        return actionService.updateAction(id, updated);
    }

    /**
     * Supprime une action par son identifiant.
     * @param id l'identifiant de l'action à supprimer.
     */
    @DeleteMapping("/{id}")
    public void deleteAction(@PathVariable Integer id) {
        actionService.deleteById(id);
    }

    /**
     * Associe un référent à une action existante.
     * @param id l'identifiant de l'action.
     * @param payload JSON contenant l'identifiant du référent.
     * @return une réponse HTTP indiquant le succès ou l'erreur.
     */
    @PutMapping("/{id}/referents")
    public ResponseEntity<?> assignReferent(
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> payload) {

        Integer idReferent = payload.get("idReferent");

        Optional<Action> actionOpt = actionRepository.findById(id);
        Optional<Referent> referentOpt = referentRepository.findById(idReferent);

        if (actionOpt.isEmpty() || referentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Action ou référent introuvable");
        }

        Action action = actionOpt.get();
        action.setReferent(referentOpt.get());
        actionRepository.save(action);

        return ResponseEntity.ok("Référent assigné avec succès !");
    }
}
