package isis.projet.backend.controller;

import isis.projet.backend.service.ParticipeService;
import isis.projet.backend.entity.Participe;
import isis.projet.backend.entity.ParticipeKey;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import jakarta.validation.Valid;

/**
 * Contrôleur pour gérer les participations des étudiants aux actions.
 */
@RestController
@RequestMapping("/api/participe")
@RequiredArgsConstructor
public class ParticipeController {

    // Injection du service
    private final ParticipeService participeService;

    /**
     * Récupère toutes les participations.
     *
     * @return liste des participations.
     */
    @GetMapping
    public List<Participe> getAllParticipations() {
        return participeService.findAll();
    }

    /**
     * Crée une nouvelle participation.
     *
     * @param participe les informations de la participation.
     * @return la participation créée.
     */
    @PostMapping
    public Participe createParticipe(@RequestBody Participe participe) {
        // Créer la clé composite à partir des identifiants des associations
        ParticipeKey key = new ParticipeKey();
        key.setIdEtudiant(participe.getEtudiant().getIdEtudiant());
        key.setIdAction(participe.getAction().getIdAction());
        key.setIdSemestre(participe.getSemestre().getIdSemestre());

        // Affecter la clé composite à l'entité Participe
        participe.setId(key);

        // Sauvegarder la participation
        return participeService.save(participe);
    }

    /**
     * Récupère une participation par sa clé composite.
     *
     * @param idEtudiant l'ID de l'étudiant.
     * @param idAction l'ID de l'action.
     * @param idSemestre l'ID du semestre.
     * @return la participation trouvée ou une erreur 404.
     */
    @GetMapping("/{idEtudiant}/{idAction}/{idSemestre}")
    public Participe getParticipationById(@PathVariable Integer idEtudiant,
                                          @PathVariable Integer idAction,
                                          @PathVariable Integer idSemestre) {
        ParticipeKey key = new ParticipeKey(idEtudiant, idAction, idSemestre);
        return participeService.findById(key);
    }

    /**
     * Met à jour une participation existante.
     *
     * @param idEtudiant l'ID de l'étudiant.
     * @param idAction l'ID de l'action.
     * @param idSemestre l'ID du semestre.
     * @param updated les nouvelles valeurs.
     * @return la participation mise à jour.
     */
    @PutMapping("/{idEtudiant}/{idAction}/{idSemestre}")
    public Participe updateParticipation(@PathVariable Integer idEtudiant,
                                         @PathVariable Integer idAction,
                                         @PathVariable Integer idSemestre,
                                         @Valid @RequestBody Participe updated) {
        ParticipeKey key = new ParticipeKey(idEtudiant, idAction, idSemestre);
        return participeService.updateParticipe(key, updated);
    }

    /**
     * Supprime une participation par sa clé composite.
     *
     * @param idEtudiant l'ID de l'étudiant.
     * @param idAction l'ID de l'action.
     * @param idSemestre l'ID du semestre.
     */
    @DeleteMapping("/{idEtudiant}/{idAction}/{idSemestre}")
    public void deleteParticipation(@PathVariable Integer idEtudiant,
                                    @PathVariable Integer idAction,
                                    @PathVariable Integer idSemestre) {
        ParticipeKey key = new ParticipeKey(idEtudiant, idAction, idSemestre);
        participeService.deleteById(key);
    }

    @GetMapping("/action/{idAction}")
    public List<Participe> getParticipationsByAction(@PathVariable Integer idAction) {
        return participeService.getParticipationsByAction(idAction);
    }
    @GetMapping("/etudiant/{idEtudiant}")
    public List<Participe> getParticipationsByEtudiant(@PathVariable Integer idEtudiant) {
        return participeService.getParticipationsByEtudiant(idEtudiant);
    }
    @GetMapping("/etudiant/{idEtudiant}/action/{idAction}")
    public List<Participe> getParticipationsByEtudiantAndAction(@PathVariable Integer idEtudiant,
                                                                @PathVariable Integer idAction) {
        return participeService.getParticipationsByEtudiantAndAction(idEtudiant, idAction);
    }
    @GetMapping("/action/{idAction}/semestre/{idSemestre}")
    public List<Participe> getEtudiantsByActionAndSemestre(@PathVariable Integer idAction,
                                                           @PathVariable Integer idSemestre) {
        return participeService.getEtudiantsByActionAndSemestre(idAction, idSemestre);
    }
    @GetMapping("/semestre/{idSemestre}")
    public List<Participe> getParticipationsBySemestre(@PathVariable Integer idSemestre) {
        return participeService.getParticipationsBySemestre(idSemestre);
    }
}