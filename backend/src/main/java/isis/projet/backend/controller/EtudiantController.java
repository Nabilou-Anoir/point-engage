package isis.projet.backend.controller;

import isis.projet.backend.dao.EtudiantRepository;
import isis.projet.backend.entity.Etudiant;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur pour gérer les étudiants.
 */
@RestController
@RequestMapping("/api/etudiants")
@RequiredArgsConstructor
public class EtudiantController {

    private final EtudiantRepository etudiantRepository;

    @GetMapping
    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }

    @PostMapping
    public Etudiant createEtudiant(@RequestBody Etudiant etudiant) {
        return etudiantRepository.save(etudiant);
    }

    @GetMapping("/{id}")
    public Etudiant getEtudiantById(@PathVariable Long id) {
        return etudiantRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Etudiant updateEtudiant(@PathVariable Long id, @RequestBody Etudiant updated) {
        return etudiantRepository.findById(id)
                .map(e -> {
                    e.setNom(updated.getNom());
                    e.setPrenom(updated.getPrenom());
                    e.setEmail(updated.getEmail());
                    e.setPromotion(updated.getPromotion());
                    return etudiantRepository.save(e);
                })
                .orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteEtudiant(@PathVariable Long id) {
        etudiantRepository.deleteById(id);
    }
}