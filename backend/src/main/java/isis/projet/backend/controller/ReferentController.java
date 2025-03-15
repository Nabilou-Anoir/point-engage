package isis.projet.backend.controller;

import isis.projet.backend.service.ReferentService;
import isis.projet.backend.entity.Referent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import jakarta.validation.Valid;

/**
 * Contrôleur pour gérer les référents.
 */
@RestController
@RequestMapping("/api/referents")
@RequiredArgsConstructor
public class ReferentController {

    // Injection du service au lieu du repository
    private final ReferentService referentService;

    /**
     * Récupère tous les référents.
     *
     * @return liste des référents.
     */
    @GetMapping
    public List<Referent> getAllReferents() {
        return referentService.findAll();
    }

    /**
     * Crée un nouveau référent.
     *
     * @param referent les informations du référent.
     * @return le référent créé.
     */
    @PostMapping
    public Referent createReferent(@Valid @RequestBody Referent referent) {
        return referentService.save(referent);
    }

    /**
     * Récupère un référent par son identifiant.
     *
     * @param id l'identifiant du référent.
     * @return le référent trouvé ou une erreur 404.
     */
    @GetMapping("/{id}")
    public Referent getReferentById(@PathVariable Integer id) {
        return referentService.findById(id);
    }

    /**
     * Met à jour un référent existant.
     *
     * @param id l'identifiant du référent à mettre à jour.
     * @param updated les nouvelles valeurs.
     * @return le référent mis à jour.
     */
    @PutMapping("/{id}")
    public Referent updateReferent(@PathVariable Integer id, @Valid @RequestBody Referent updated) {
        return referentService.updateReferent(id, updated);
    }

    /**
     * Supprime un référent par son identifiant.
     *
     * @param id l'identifiant du référent à supprimer.
     */
    @DeleteMapping("/{id}")
    public void deleteReferent(@PathVariable Integer id) {
        referentService.deleteById(id);
    }
}