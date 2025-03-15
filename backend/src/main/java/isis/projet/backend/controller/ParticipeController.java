package isis.projet.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import isis.projet.backend.service.ParticipeService;
import isis.projet.backend.entity.Participe;
import isis.projet.backend.entity.ParticipeKey;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/participe")
@RequiredArgsConstructor
@Tag(name = "Gestion des Participations", description = "API pour gérer les participations des étudiants aux actions")
public class ParticipeController {

    private final ParticipeService participeService;

    @Operation(summary = "Récupérer toutes les participations")
    @GetMapping
    public ResponseEntity<List<Participe>> getAllParticipations() {
        return ResponseEntity.ok(participeService.findAll());
    }

    @Operation(summary = "Créer une nouvelle participation")
    @PostMapping
    public ResponseEntity<Participe> createParticipe(@Valid @RequestBody Participe participe) {
        if (participe.getEtudiant() == null || participe.getAction() == null || participe.getSemestre() == null) {
            return ResponseEntity.badRequest().build();
        }

        ParticipeKey key = new ParticipeKey(
                participe.getEtudiant().getIdEtudiant(),
                participe.getAction().getIdAction(),
                participe.getSemestre().getIdSemestre()
        );
        participe.setId(key);

        return ResponseEntity.status(HttpStatus.CREATED).body(participeService.save(participe));
    }

    @Operation(summary = "Récupérer une participation par ID")
    @GetMapping("/{idEtudiant}/{idAction}/{idSemestre}")
    public ResponseEntity<Participe> getParticipationById(
            @PathVariable Integer idEtudiant,
            @PathVariable Integer idAction,
            @PathVariable Integer idSemestre) {

        ParticipeKey key = new ParticipeKey(idEtudiant, idAction, idSemestre);
        return ResponseEntity.ok(participeService.findById(key));
    }

    @Operation(summary = "Mettre à jour une participation")
    @PutMapping("/{idEtudiant}/{idAction}/{idSemestre}")
    public ResponseEntity<Participe> updateParticipation(
            @PathVariable Integer idEtudiant,
            @PathVariable Integer idAction,
            @PathVariable Integer idSemestre,
            @Valid @RequestBody Participe updated) {

        ParticipeKey key = new ParticipeKey(idEtudiant, idAction, idSemestre);
        return ResponseEntity.ok(participeService.updateParticipe(key, updated));
    }

    @Operation(summary = "Supprimer une participation par ID")
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
        return ResponseEntity.ok(participeService.getParticipationsByAction(idAction));
    }

    @Operation(summary = "Récupérer les participations par étudiant")
    @GetMapping("/etudiant/{idEtudiant}")
    public ResponseEntity<List<Participe>> getParticipationsByEtudiant(@PathVariable Integer idEtudiant) {
        return ResponseEntity.ok(participeService.getParticipationsByEtudiant(idEtudiant));
    }
}