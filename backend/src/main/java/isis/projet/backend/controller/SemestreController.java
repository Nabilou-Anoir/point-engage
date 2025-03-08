package isis.projet.backend.controller;

import isis.projet.backend.dao.SemestreRepository;
import isis.projet.backend.entity.Semestre;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur pour gérer les semestres.
 */
@RestController
@RequestMapping("/api/semestres")
@RequiredArgsConstructor
public class SemestreController {

    private final SemestreRepository semestreRepository;

    @GetMapping
    public List<Semestre> getAllSemestres() {
        return semestreRepository.findAll();
    }

    @PostMapping
    public Semestre createSemestre(@RequestBody Semestre semestre) {
        return semestreRepository.save(semestre);
    }

    @GetMapping("/{id}")
    public Semestre getSemestreById(@PathVariable Integer id) {
        return semestreRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Semestre updateSemestre(@PathVariable Integer id, @RequestBody Semestre updated) {
        return semestreRepository.findById(id)
                .map(s -> {
                    s.setDateDebutSemestre(updated.getDateDebutSemestre());
                    s.setDateFinSemestre(updated.getDateFinSemestre());
                    s.setNbSemestre(updated.getNbSemestre());
                    return semestreRepository.save(s);
                })
                .orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteSemestre(@PathVariable Integer id) {
        semestreRepository.deleteById(id);
    }
}