package isis.projet.backend.controller;

import isis.projet.backend.entity.Utilisateur;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ScolariteController {

    @GetMapping("/scolarite/home")
    public String scolariteHome(HttpSession session, Model model) {
        Utilisateur user = (Utilisateur) session.getAttribute("loggedInUser");
        if (user == null || user.getRole() == null || !user.getRole().getName().equals("ROLE_SERVICE_SCOLARITE")) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "home_scolarite";  // Renvoie la vue dédiée au service scolarité (par exemple home_scolarite.html)
    }
}