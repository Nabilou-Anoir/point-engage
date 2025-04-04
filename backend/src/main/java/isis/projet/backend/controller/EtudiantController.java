package isis.projet.backend.controller;

import isis.projet.backend.entity.Utilisateur;
import isis.projet.backend.service.EtudiantService;
import isis.projet.backend.entity.Etudiant;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping("/etudiant/AccueilEtudiantView")
    public String etudianthome (HttpSession session, Model model) {
        Utilisateur user = (Utilisateur) session.getAttribute("loggedInUser");
        if (user == null || user.getRole() == null || !user.getRole().getName().equals("ROLE_ETUDIANT")) {
            // Si l'utilisateur n'est pas connecté ou n'est pas directeur, redirigez vers la page de connexion
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "AccueilEtudiantView";  // Renvoie la vue dédiée au directeur (par exemple home_directeur.html)
    }
    @GetMapping("/byEmail")
    public ResponseEntity<Etudiant> getEtudiantByEmail(@RequestParam String email) {
        System.out.println("🔍 API appelée avec email: " + email);
        Etudiant etudiant = etudiantService.findByEmail(email);

        if (etudiant == null) {
            System.out.println("❌ Étudiant non trouvé !");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(etudiant);
    }
    @GetMapping("/{id}/totalPoints")
    public ResponseEntity<BigDecimal> getTotalPoints(@PathVariable Integer id) {
        BigDecimal totalPoints = etudiantService.calculateTotalPoints(id);
        return ResponseEntity.ok(totalPoints);
    }
    @GetMapping("/allPoints")
    public ResponseEntity<List<Map<String, Object>>> getAllStudentsWithPoints() {
        List<Etudiant> students = etudiantService.findAll();
        List<Map<String, Object>> studentPoints = students.stream().map(etudiant -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", etudiant.getIdEtudiant());
            map.put("nom", etudiant.getNom());
            map.put("prenom", etudiant.getPrenom());
            map.put("promotion", etudiant.getPromotion());
            // Utilisation de votre méthode existante pour calculer le total des points
            map.put("points", etudiantService.calculateTotalPoints(etudiant.getIdEtudiant()));
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(studentPoints);
    }
}