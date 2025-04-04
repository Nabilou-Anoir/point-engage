package isis.projet.backend.controller;

import isis.projet.backend.dao.UtilisateurRepository;
import isis.projet.backend.entity.Utilisateur;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DirecteurController {

    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public DirecteurController(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @GetMapping("/directeur/AccueilDirecteurView")
    public String directeurHome(HttpSession session, Model model) {
        Utilisateur user = (Utilisateur) session.getAttribute("loggedInUser");
        if (user == null || user.getRole() == null || !user.getRole().getName().equals("ROLE_DIRECTEUR")) {
            // Si l'utilisateur n'est pas connecté ou n'est pas directeur, rediriger vers la page de connexion
            return "redirect:/login";
        }
        model.addAttribute("user", user);

        // Optionnel : Pour afficher la liste de tous les directeurs
        // List<Utilisateur> directeurs = utilisateurRepository.findByRoleName("ROLE_DIRECTEUR");
        // model.addAttribute("directeurs", directeurs);

        return "AccueilDirecteurView";  // Renvoie la vue dédiée au directeur (ex. AccueilDirecteurView.html)
    }
}