package isis.projet.backend.service;

import isis.projet.backend.entity.Etudiant;
import isis.projet.backend.dao.EtudiantRepository;
import isis.projet.backend.entity.Participe;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Service pour la gestion des entités Etudiant.
 *
 * Fournit des méthodes CRUD pour manipuler les objets Etudiant.
 */
@Service
@RequiredArgsConstructor
public class EtudiantService {

    // Injection de la dépendance etudiantRepository.
    private final EtudiantRepository etudiantRepository;

    /**
     * Récupère tous les étudiants de la base de données.
     *
     * @return une liste de tous les étudiants.
     */
    public List<Etudiant> findAll() {
        return etudiantRepository.findAll();
    }

    /**
     * Recherche un étudiant par son identifiant.
     *
     * @param id l'identifiant de l'étudiant.
     * @return l'étudiant trouvé ou génère une erreur 404.
     */
    public Etudiant findById(Integer id) {
        return etudiantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Étudiant non trouvé avec id : " + id));
    }

    /**
     * Sauvegarde ou met à jour un étudiant dans la base de données.
     *
     * @param etudiant l'objet Etudiant à sauvegarder.
     * @return l'étudiant sauvegardé.
     */
    public Etudiant save(Etudiant etudiant) {
        return etudiantRepository.save(etudiant);
    }

    /**
     * Met à jour un étudiant existant.
     *
     * @param id l'identifiant de l'étudiant à mettre à jour.
     * @param updated l'objet contenant les nouvelles valeurs.
     * @return l'étudiant mis à jour.
     */
    public Etudiant updateEtudiant(Integer id, Etudiant updated) {
        return etudiantRepository.findById(id)
                .map(etudiant -> {
                    etudiant.setNom(updated.getNom());
                    etudiant.setPrenom(updated.getPrenom());
                    etudiant.setEmail(updated.getEmail());
                    etudiant.setPromotion(updated.getPromotion());
                    return etudiantRepository.save(etudiant);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Étudiant non trouvé avec id : " + id));
    }

    /**
     * Supprime un étudiant de la base de données par son identifiant.
     *
     * @param id l'identifiant de l'étudiant à supprimer.
     */
    public void deleteById(Integer id) {
        if (!etudiantRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Étudiant non trouvé avec id : " + id);
        }
        etudiantRepository.deleteById(id);
    }
    public Etudiant findByEmail(String email) {
        return etudiantRepository.findByEmail(email);
    }

    public BigDecimal calculateTotalPoints(Integer etudiantId) {
        Etudiant etudiant = findById(etudiantId);
        return etudiant.getParticipations().stream()
                .map(Participe::getTotalPoints)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}