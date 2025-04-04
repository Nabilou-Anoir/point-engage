package isis.projet.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isis.projet.backend.entity.Referentiel;
import isis.projet.backend.service.ReferentielService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Contrôleur pour gérer les référentiels.
 */
@RestController
@RequestMapping("/api/referentiels")
@RequiredArgsConstructor
public class ReferentielController {

    // Injection du service au lieu du repository
    private final ReferentielService referentielService;

    /**
     * Récupère tous les référentiels.
     *
     * @return liste des référentiels.
     */
    @GetMapping
    public List<Referentiel> getAllReferentiels() {
        return referentielService.findAll();
    }

    /**
     * Crée un nouveau référentiel.
     *
     * @param referentiel les informations du référentiel.
     * @return le référentiel créé.
     */
    @PostMapping
    public Referentiel createReferentiel(@Valid @RequestBody Referentiel referentiel) {
        return referentielService.save(referentiel);
    }

    /**
     * Récupère un référentiel par son identifiant.
     *
     * @param id l'identifiant du référentiel.
     * @return le référentiel trouvé ou une erreur 404.
     */
    @GetMapping("/{id}")
    public Referentiel getReferentielById(@PathVariable Integer id) {
        return referentielService.findById(id);
    }

    /**
     * Met à jour un référentiel existant.
     *
     * @param id l'identifiant du référentiel à mettre à jour.
     * @param updated les nouvelles valeurs.
     * @return le référentiel mis à jour.
     */
    @PutMapping("/{id}")
    public Referentiel updateReferentiel(@PathVariable Integer id, @Valid @RequestBody Referentiel updated) {
        return referentielService.updateReferentiel(id, updated);
    }

    /**
     * Supprime un référentiel par son identifiant.
     *
     * @param id l'identifiant du référentiel à supprimer.
     */
    @DeleteMapping("/{id}")
    public void deleteReferentiel(@PathVariable Integer id) {
        referentielService.deleteById(id);
    }
}