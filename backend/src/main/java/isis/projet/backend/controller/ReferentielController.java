package isis.projet.backend.controller;

import isis.projet.backend.dao.ReferentielRepository;
import isis.projet.backend.entity.Referentiel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur pour gérer les référentiels.
 */
@RestController
@RequestMapping("/api/referentiels")
@RequiredArgsConstructor
public class ReferentielController {

    private final ReferentielRepository referentielRepository;

    @GetMapping
    public List<Referentiel> getAllReferentiels() {
        return referentielRepository.findAll();
    }

    @PostMapping
    public Referentiel createReferentiel(@RequestBody Referentiel referentiel) {
        return referentielRepository.save(referentiel);
    }

    @GetMapping("/{id}")
    public Referentiel getReferentielById(@PathVariable Integer id) {
        return referentielRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Referentiel updateReferentiel(@PathVariable Integer id, @RequestBody Referentiel updated) {
        return referentielRepository.findById(id)
                .map(r -> {
                    r.setNom(updated.getNom());
                    r.setDescription(updated.getDescription());
                    return referentielRepository.save(r);
                })
                .orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteReferentiel(@PathVariable Integer id) {
        referentielRepository.deleteById(id);
    }
}