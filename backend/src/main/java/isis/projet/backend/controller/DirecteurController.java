package isis.projet.backend.controller;

import isis.projet.backend.entity.Utilisateur;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DirecteurController {

    @GetMapping("/directeur/AccueilDirecteurView")
    public String directeurHome(HttpSession session, Model model) {
        Utilisateur user = (Utilisateur) session.getAttribute("loggedInUser");
        if (user == null || user.getRole() == null || !user.getRole().getName().equals("ROLE_DIRECTEUR")) {
            // Si l'utilisateur n'est pas connecté ou n'est pas directeur, redirigez vers la page de connexion
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "AccueilDirecteurView";  // Renvoie la vue dédiée au directeur (par exemple home_directeur.html)
    }
}