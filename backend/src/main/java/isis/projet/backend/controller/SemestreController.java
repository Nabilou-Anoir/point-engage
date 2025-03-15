package isis.projet.backend.controller;

import isis.projet.backend.service.SemestreService;
import isis.projet.backend.entity.Semestre;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import jakarta.validation.Valid;

/**
 * Contrôleur pour gérer les semestres.
 */
@RestController
@RequestMapping("/api/semestres")
@RequiredArgsConstructor
public class SemestreController {

    // Injection du service au lieu du repository
    private final SemestreService semestreService;

    /**
     * Récupère tous les semestres.
     *
     * @return liste des semestres.
     */
    @GetMapping
    public List<Semestre> getAllSemestres() {
        return semestreService.findAll();
    }

    /**
     * Crée un nouveau semestre.
     *
     * @param semestre les informations du semestre.
     * @return le semestre créé.
     */
    @PostMapping
    public Semestre createSemestre(@Valid @RequestBody Semestre semestre) {
        return semestreService.save(semestre);
    }

    /**
     * Récupère un semestre par son identifiant.
     *
     * @param id l'identifiant du semestre.
     * @return le semestre trouvé ou une erreur 404.
     */
    @GetMapping("/{id}")
    public Semestre getSemestreById(@PathVariable Integer id) {
        return semestreService.findById(id);
    }

    /**
     * Met à jour un semestre existant.
     *
     * @param id l'identifiant du semestre à mettre à jour.
     * @param updated les nouvelles valeurs.
     * @return le semestre mis à jour.
     */
    @PutMapping("/{id}")
    public Semestre updateSemestre(@PathVariable Integer id, @Valid @RequestBody Semestre updated) {
        return semestreService.updateSemestre(id, updated);
    }

    /**
     * Supprime un semestre par son identifiant.
     *
     * @param id l'identifiant du semestre à supprimer.
     */
    @DeleteMapping("/{id}")
    public void deleteSemestre(@PathVariable Integer id) {
        semestreService.deleteById(id);
    }
}