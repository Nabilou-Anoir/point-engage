package isis.projet.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import isis.projet.backend.entity.Referent;
import isis.projet.backend.service.ReferentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Contrôleur pour gérer les référents.
 */
@RestController
@RequestMapping("/api/referents")
@RequiredArgsConstructor
public class ReferentController {

    // Injection du service au lieu du repository
    private final ReferentService referentService;

    /**
     * Récupère tous les référents.
     *
     * @return liste des référents.
     */
    @GetMapping
    public List<Referent> getAllReferents() {
        return referentService.findAll();
    }

    /**
     * Crée un nouveau référent.
     *
     * @param referent les informations du référent.
     * @return le référent créé.
     */
    @PostMapping
    public Referent createReferent(@Valid @RequestBody Referent referent) {
        return referentService.save(referent);
    }

    /**
     * Récupère un référent par son identifiant.
     *
     * @param id l'identifiant du référent.
     * @return le référent trouvé ou une erreur 404.
     */
    @GetMapping("/{id}")
    public Referent getReferentById(@PathVariable Integer id) {
        return referentService.findById(id);
    }

    /**
     * Met à jour un référent existant.
     *
     * @param id l'identifiant du référent à mettre à jour.
     * @param updated les nouvelles valeurs.
     * @return le référent mis à jour.
     */
    @PutMapping("/{id}")
    public Referent updateReferent(@PathVariable Integer id, @Valid @RequestBody Referent updated) {
        return referentService.updateReferent(id, updated);
    }

    /**
     * Supprime un référent par son identifiant.
     *
     * @param id l'identifiant du référent à supprimer.
     */
    @DeleteMapping("/{id}")
    public void deleteReferent(@PathVariable Integer id) {
        referentService.deleteById(id);
    }
// Updated upstream
//
//    /**
//     * Renvoie la page d'accueil du référent.
//     *
//     * @param session la session HTTP.
//     * @param model   le modèle pour la vue.
//     * @return la vue de la page d'accueil du référent.
//     */
//
//    @GetMapping("/referent/home")
//    public String referentHome(HttpSession session, Model model) {
//        Utilisateur user = (Utilisateur) session.getAttribute("loggedInUser");
//        if (user == null || user.getRole() == null || !user.getRole().getName().equals("ROLE_REFERENT")) {
//            return "redirect:/login";
//        }
//        model.addAttribute("user", user);
//        return "home_referent";  // Renvoie la vue dédiée au référent (par exemple home_referent.html)
//    }

    /**
     * Renvoie la page d'accueil du référent.
     *
     * @param session la session HTTP.
     * @param model   le modèle pour la vue.
     * @return la vue de la page d'accueil du référent.
     */

   // @GetMapping("/referent/home")
    // public String referentHome(HttpSession session, Model model) {
      //  Utilisateur user = (Utilisateur) session.getAttribute("loggedInUser");
       // if (user == null || user.getRole() == null || !user.getRole().getName().equals("ROLE_REFERENT")) {
      //      return "redirect:/login";
       // }
       // model.addAttribute("user", user);
       // return "home_referent";  // Renvoie la vue dédiée au référent (par exemple home_referent.html)
    //}
// Stashed changes
}