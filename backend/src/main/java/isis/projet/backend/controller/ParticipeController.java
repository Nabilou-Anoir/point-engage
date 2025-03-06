package isis.projet.backend.controller;

import isis.projet.backend.dao.ParticipeRepository;
import isis.projet.backend.entity.Participe;
import isis.projet.backend.entity.ParticipeKey;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur pour gérer les participations (Participe).
 */
@RestController
@RequestMapping("/api/participes")
@RequiredArgsConstructor
public class ParticipeController {

    private final ParticipeRepository participeRepository;

    @GetMapping
    public List<Participe> getAllParticipes() {
        return participeRepository.findAll();
    }
    @PostMapping
    public Participe createParticipe(@RequestBody Participe participe) {
        // Créer la clé composite à partir des identifiants des associations
        ParticipeKey key = new ParticipeKey();
        key.setIdEtudiant(participe.getEtudiant().getIdEtudiant());
        key.setIdAction(participe.getAction().getIdAction());
        key.setIdSemestre(participe.getSemestre().getIdSemestre());
        // Affecter la clé composite à l'entité
        participe.setId(key);
        return participeRepository.save(participe);
    }
    /**
     * Récupère une participation via sa clé composite (idEtudiant, idAction, idSemestre).
     */
    @GetMapping("/{idEtudiant}/{idAction}/{idSemestre}")
    public Participe getParticipeByIds(@PathVariable Integer idEtudiant,
                                       @PathVariable Integer idAction,
                                       @PathVariable Integer idSemestre) {
        ParticipeKey key = new ParticipeKey(idEtudiant, idAction, idSemestre);
        return participeRepository.findById(key).orElse(null);
    }

    /**
     * Met à jour une participation.
     */
    @PutMapping("/{idEtudiant}/{idAction}/{idSemestre}")
    public Participe updateParticipe(@PathVariable Integer idEtudiant,
                                     @PathVariable Integer idAction,
                                     @PathVariable Integer idSemestre,
                                     @RequestBody Participe updated) {
        ParticipeKey key = new ParticipeKey(idEtudiant, idAction, idSemestre);
        return participeRepository.findById(key)
                .map(p -> {
                    p.setTotalPoints(updated.getTotalPoints());
                    p.setNbParticipation(updated.getNbParticipation());
                    p.setDescriptionParticipation(updated.getDescriptionParticipation());
                    p.setDateFinParticipation(updated.getDateFinParticipation());
                    p.setDateDebutParticipation(updated.getDateDebutParticipation());
                    return participeRepository.save(p);
                })
                .orElse(null);
    }

    /**
     * Supprime une participation.
     */
    @DeleteMapping("/{idEtudiant}/{idAction}/{idSemestre}")
    public void deleteParticipe(@PathVariable Integer idEtudiant,
                                @PathVariable Integer idAction,
                                @PathVariable Integer idSemestre) {
        ParticipeKey key = new ParticipeKey(idEtudiant, idAction, idSemestre);
        participeRepository.deleteById(key);
    }
}