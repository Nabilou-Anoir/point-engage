package isis.projet.backend.controller;

import isis.projet.backend.dao.UtilisateurRepository;
import isis.projet.backend.dao.RoleRepository;
import isis.projet.backend.dao.EtudiantRepository;
import isis.projet.backend.dao.ReferentRepository;
import isis.projet.backend.entity.Utilisateur;
import isis.projet.backend.entity.Role;
import isis.projet.backend.entity.Etudiant;
import isis.projet.backend.entity.Referent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final EtudiantRepository etudiantRepository;
    private final ReferentRepository referentRepository;

    public AuthController(UtilisateurRepository utilisateurRepository,
                          RoleRepository roleRepository,
                          EtudiantRepository etudiantRepository,
                          ReferentRepository referentRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
        this.etudiantRepository = etudiantRepository;
        this.referentRepository = referentRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Utilisateur newUser) {
        // Vérifier si l'email est déjà utilisé
        if (utilisateurRepository.findByEmail(newUser.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email déjà utilisé.");
        }

        // Vérifier si le username est vide
        if (newUser.getUsername() == null || newUser.getUsername().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le champ username est obligatoire.");
        }

        // Vérifier si le username est déjà pris
        if (utilisateurRepository.findByUsername(newUser.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nom d'utilisateur déjà utilisé.");
        }

        // Déterminer le rôle à partir de l'email
        String email = newUser.getEmail().toLowerCase();
        String roleName;

        if (email.equals("adrien.defossez@univ-jfc.fr")) {
            roleName = "ROLE_DIRECTEUR";
        } else if (email.equals("scolarite-isis@univ-jfc.fr")) {
            roleName = "ROLE_SERVICE_SCOLARITE";
        } else if (email.endsWith("@etud.univ-jfc.fr")) {
            roleName = "ROLE_ETUDIANT";
        } else if (email.endsWith("@univ-jfc.fr")) {
            roleName = "ROLE_REFERENT";
        } else {
            roleName = "ROLE_ETUDIANT";
        }

        // Vérifier si le rôle existe
        Optional<Role> roleOpt = roleRepository.findByName(roleName);
        if (roleOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Rôle non trouvé.");
        }

        newUser.setRole(roleOpt.get());

        // Sauvegarde de l'utilisateur dans la base de données
        Utilisateur savedUser = utilisateurRepository.save(newUser);

        // Ajouter l'utilisateur dans la table correspondante à son rôle
        if ("ROLE_ETUDIANT".equals(roleName)) {
            Etudiant etudiant = new Etudiant();
            etudiant.setNom(newUser.getNom());
            etudiant.setPrenom(newUser.getPrenom());
            etudiant.setEmail(newUser.getEmail());
            etudiant.setPromotion(newUser.getPromotion());
            etudiantRepository.save(etudiant);
        } else if ("ROLE_REFERENT".equals(roleName)) {
            Referent referent = new Referent();
            referent.setNom(newUser.getNom());
            referent.setPrenom(newUser.getPrenom());
            referent.setEmail(newUser.getEmail());
            referentRepository.save(referent);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Utilisateur inscrit avec succès !");
    }

    @PostMapping("/registerDirector")
    public ResponseEntity<?> registerDirector(@RequestBody Utilisateur newUser) {
        // Vérifier si l'email est déjà utilisé
        if (utilisateurRepository.findByEmail(newUser.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email déjà utilisé.");
        }

        // Vérifier si le username est vide
        if (newUser.getUsername() == null || newUser.getUsername().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le champ username est obligatoire.");
        }

        // Vérifier si le username est déjà pris
        if (utilisateurRepository.findByUsername(newUser.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nom d'utilisateur déjà utilisé.");
        }

        // Forcer l'attribution du rôle directeur
        Optional<Role> roleOpt = roleRepository.findByName("ROLE_DIRECTEUR");
        if (roleOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Rôle non trouvé.");
        }

        newUser.setRole(roleOpt.get());

        // Sauvegarder l'utilisateur dans la base de données
        utilisateurRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("Directeur inscrit avec succès !");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<Utilisateur> userOpt = utilisateurRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non trouvé.");
        }

        Utilisateur user = userOpt.get();

        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mot de passe incorrect.");
        }

        return ResponseEntity.ok(user);
    }
}