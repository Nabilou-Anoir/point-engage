package isis.projet.backend.controller;
import isis.projet.backend.dao.ReferentRepository;
import isis.projet.backend.entity.Referent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/referents")
@RequiredArgsConstructor
public class ReferentController {

    private final ReferentRepository referentRepository;

    @GetMapping
    public List<Referent> getAllReferents() {
        return referentRepository.findAll();
    }

    @PostMapping
    public Referent createReferent(@RequestBody Referent referent) {
        return referentRepository.save(referent);
    }

    @GetMapping("/{id}")
    public Referent getReferentById(@PathVariable Integer id) {
        return referentRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Referent updateReferent(@PathVariable Integer id, @RequestBody Referent updated) {
        return referentRepository.findById(id)
                .map(r -> {
                    r.setNom(updated.getNom());
                    r.setPrenom(updated.getPrenom());
                    r.setEmail(updated.getEmail());
                    return referentRepository.save(r);
                })
                .orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteReferent(@PathVariable Integer id) {
        referentRepository.deleteById(id);
    }
}