package isis.projet.backend.controller;

import isis.projet.backend.service.EtudiantService;
import isis.projet.backend.entity.Etudiant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import jakarta.validation.Valid;

/**
 * Contrôleur pour gérer les étudiants.
 */
@RestController
@RequestMapping("/api/etudiants")
@RequiredArgsConstructor
public class EtudiantController {

    // Injection du service et non du repository
    private final EtudiantService etudiantService;

    /**
     * Récupère tous les étudiants.
     *
     * @return liste des étudiants.
     */
    @GetMapping
    public List<Etudiant> getAllEtudiants() {
        return etudiantService.findAll();
    }

    /**
     * Crée un nouvel étudiant.
     *
     * @param etudiant les informations de l'étudiant.
     * @return l'étudiant créé.
     */
    @PostMapping
    public Etudiant createEtudiant(@Valid @RequestBody Etudiant etudiant) {
        return etudiantService.save(etudiant);
    }

    /**
     * Récupère un étudiant par son identifiant.
     *
     * @param id l'identifiant de l'étudiant.
     * @return l'étudiant trouvé ou une erreur 404.
     */
    @GetMapping("/{id}")
    public Etudiant getEtudiantById(@PathVariable Integer id) {
        return etudiantService.findById(id);
    }

    /**
     * Met à jour un étudiant existant.
     *
     * @param id l'identifiant de l'étudiant à mettre à jour.
     * @param updated les nouvelles valeurs.
     * @return l'étudiant mis à jour.
     */
    @PutMapping("/{id}")
    public Etudiant updateEtudiant(@PathVariable Integer id, @Valid @RequestBody Etudiant updated) {
        return etudiantService.updateEtudiant(id, updated);
    }

    /**
     * Supprime un étudiant par son identifiant.
     *
     * @param id l'identifiant de l'étudiant à supprimer.
     */
    @DeleteMapping("/{id}")
    public void deleteEtudiant(@PathVariable Integer id) {
        etudiantService.deleteById(id);
    }
}