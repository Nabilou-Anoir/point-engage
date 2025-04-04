package isis.projet.backend.service;

import isis.projet.backend.dao.RoleRepository;
import isis.projet.backend.dao.UtilisateurRepository;
import isis.projet.backend.entity.Role;
import isis.projet.backend.entity.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    // Créer un utilisateur avec rôle auto-déduit
    public Optional<Utilisateur> registerUser(Utilisateur utilisateur) {
        if (utilisateurRepository.findByEmail(utilisateur.getEmail()).isPresent()) {
            return Optional.empty();
        }

        String email = utilisateur.getEmail();
        Role role = roleRepository.findByName(
                email.endsWith("@etud.univ-jfc.fr") ? "ROLE_ETUDIANT" :
                        email.endsWith("@univ-jfc.fr") ? "ROLE_REFERENT" :
                                "ROLE_SERVICE_SCOLARITE"
        ).orElse(null);

        if (role == null) {
            return Optional.empty();
        }

        utilisateur.setRole(role);
        Utilisateur saved = utilisateurRepository.save(utilisateur);
        return Optional.of(saved);
    }

    // Connexion utilisateur
    public Optional<Utilisateur> login(String email, String password) {
        return utilisateurRepository.findByEmailAndPassword(email, password);
    }

    // Lire tous les utilisateurs
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    // Lire un utilisateur par ID
    public Optional<Utilisateur> getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id);
    }

    // Modifier un utilisateur
    public Optional<Utilisateur> updateUtilisateur(Long id, Utilisateur updated) {
        return utilisateurRepository.findById(id).map(existing -> {
            existing.setUsername(updated.getUsername());
            existing.setEmail(updated.getEmail());
            existing.setNom(updated.getNom());
            existing.setPrenom(updated.getPrenom());
            existing.setPassword(updated.getPassword());
            existing.setPromotion(updated.getPromotion());
            existing.setRole(updated.getRole());
            return utilisateurRepository.save(existing);
        });
    }

    // Supprimer un utilisateur
    public boolean deleteUtilisateur(Long id) {
        if (utilisateurRepository.existsById(id)) {
            utilisateurRepository.deleteById(id);
            return true;
        }
        return false;
    }
}