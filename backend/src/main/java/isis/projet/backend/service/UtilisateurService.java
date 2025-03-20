package isis.projet.backend.service;

import isis.projet.backend.dao.RoleRepository;
import isis.projet.backend.dao.UtilisateurRepository;
import isis.projet.backend.entity.Role;
import isis.projet.backend.entity.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Enregistre un utilisateur en attribuant un rôle automatiquement selon l'email.
     * @param utilisateur - L'utilisateur à enregistrer
     * @return Un `Optional<Utilisateur>` contenant l'utilisateur enregistré, ou vide si l'email existe déjà.
     */
    public Optional<Utilisateur> registerUser(Utilisateur utilisateur) {
        if (utilisateurRepository.findByEmail(utilisateur.getEmail()).isPresent()) {
            return Optional.empty(); // L'email est déjà utilisé
        }

        // Déterminer le rôle en fonction de l'email
        String email = utilisateur.getEmail();
        Role role = roleRepository.findByName(email.endsWith("@etud.univ-jfc.fr") ? "ROLE_ETUDIANT" :
                email.endsWith("@univ-jfc.fr") ? "ROLE_REFERENT" :
                        "ROLE_SERVICE_SCOLARITE").orElse(null);

        if (role == null) {
            return Optional.empty(); // Aucun rôle trouvé
        }

        utilisateur.setRole(role);
        utilisateurRepository.save(utilisateur);

        return Optional.of(utilisateur);
    }

    /**
     * Vérifie les identifiants d'un utilisateur.
     * @param email - Email de l'utilisateur
     * @param password - Mot de passe (non crypté ici)
     * @return Un `Optional<Utilisateur>` si les identifiants sont valides, sinon `Optional.empty()`.
     */
    public Optional<Utilisateur> login(String email, String password) {
        return utilisateurRepository.findByEmailAndPassword(email, password);
    }
}