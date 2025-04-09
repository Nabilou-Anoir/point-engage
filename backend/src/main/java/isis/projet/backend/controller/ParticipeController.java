package isis.projet.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import isis.projet.backend.dao.ParticipeRepository;
import isis.projet.backend.entity.Participe;
import isis.projet.backend.entity.ParticipeKey;
import isis.projet.backend.service.ParticipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/participes")
@RequiredArgsConstructor
@Tag(name = "Gestion des Participations", description = "API pour gérer les participations des étudiants aux actions")
public class ParticipeController {

    private final ParticipeService participeService;
    private final ParticipeRepository participeRepository;

    @Operation(summary = "Récupérer toutes les participations")
    @GetMapping
    public ResponseEntity<List<Participe>> getAllParticipations() {
        return ResponseEntity.ok(participeService.findAll());
    }

    @Operation(summary = "Créer une nouvelle participation")
    @PostMapping
    public ResponseEntity<Participe> createParticipe(@Valid @RequestBody Participe participe) {
        // Vérification de la présence des associations obligatoires
        if (participe.getEtudiant() == null || participe.getAction() == null || participe.getSemestre() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Construction de la clé composite à partir des identifiants fournis
        ParticipeKey key = new ParticipeKey(
                participe.getEtudiant().getIdEtudiant(),
                participe.getAction().getIdAction(),
                participe.getSemestre().getIdSemestre()
        );
        participe.setId(key);

        Participe saved = participeService.save(participe);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Récupérer une participation par sa clé composite")
    @GetMapping("/{idEtudiant}/{idAction}/{idSemestre}")
    public ResponseEntity<Participe> getParticipationById(
            @PathVariable Integer idEtudiant,
            @PathVariable Integer idAction,
            @PathVariable Integer idSemestre) {

        ParticipeKey key = new ParticipeKey(idEtudiant, idAction, idSemestre);
        Participe participe = participeService.findById(key);
        if (participe == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(participe);
    }

    @Operation(summary = "Mettre à jour une participation")
    @PutMapping("/{idEtudiant}/{idAction}/{idSemestre}")
    public ResponseEntity<Participe> updateParticipation(
            @PathVariable Integer idEtudiant,
            @PathVariable Integer idAction,
            @PathVariable Integer idSemestre,
            @Valid @RequestBody Participe updated) {

        ParticipeKey key = new ParticipeKey(idEtudiant, idAction, idSemestre);
        Participe updatedParticipe = participeService.updateParticipe(key, updated);
        return ResponseEntity.ok(updatedParticipe);
    }

    @Operation(summary = "Supprimer une participation par sa clé composite")
    @DeleteMapping("/{idEtudiant}/{idAction}/{idSemestre}")
    public ResponseEntity<Void> deleteParticipation(
            @PathVariable Integer idEtudiant,
            @PathVariable Integer idAction,
            @PathVariable Integer idSemestre) {

        ParticipeKey key = new ParticipeKey(idEtudiant, idAction, idSemestre);
        participeService.deleteById(key);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Récupérer les participations par action")
    @GetMapping("/action/{idAction}")
    public ResponseEntity<List<Participe>> getParticipationsByAction(@PathVariable Integer idAction) {
        List<Participe> participes = participeService.getParticipationsByAction(idAction);
        return ResponseEntity.ok(participes);
    }

    @Operation(summary = "Récupérer les participations par étudiant")
    @GetMapping("/etudiant/{idEtudiant}")
    public ResponseEntity<List<Participe>> getParticipationsByEtudiant(@PathVariable Integer idEtudiant) {
        List<Participe> participes = participeService.getParticipationsByEtudiant(idEtudiant);
        return ResponseEntity.ok(participes);
    }

    @Operation(summary = "Récupérer les participations par année universitaire et semestre")
    @GetMapping("/byYearAndSemestre")
    public ResponseEntity<List<Participe>> getParticipationsByYearAndSemestre(
            @RequestParam Integer year,
            @RequestParam Integer semestre) {
        List<Participe> participes = participeRepository.findBySemestreAndYear(semestre, year);
        return ResponseEntity.ok(participes);
    }
}