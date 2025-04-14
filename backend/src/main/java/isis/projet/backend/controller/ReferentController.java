package isis.projet.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import isis.projet.backend.entity.Referent;
import isis.projet.backend.service.ReferentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/referents")
@RequiredArgsConstructor
public class ReferentController {

    private final ReferentService referentService;

    @GetMapping
    public List<Referent> getAllReferents() {
        return referentService.findAll();
    }

    @PostMapping
    public Referent createReferent(@Valid @RequestBody Referent referent) {
        return referentService.save(referent);
    }

    @GetMapping("/{id}")
    public Referent getReferentById(@PathVariable Integer id) {
        return referentService.findById(id);
    }

    @PutMapping("/{id}")
    public Referent updateReferent(@PathVariable Integer id, @Valid @RequestBody Referent updated) {
        return referentService.updateReferent(id, updated);
    }

    @DeleteMapping("/{id}")
    public void deleteReferent(@PathVariable Integer id) {
        referentService.deleteById(id);
    }

    /**
     * ✅ Récupère un référent par son email (utilisé pour stockage du referentId à la connexion)
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Referent> getReferentByEmail(@PathVariable String email) {
        Optional<Referent> ref = referentService.findByEmail(email);
        return ref.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}