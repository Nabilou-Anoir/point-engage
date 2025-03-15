package isis.projet.backend.service;

import isis.projet.backend.entity.Referent;
import isis.projet.backend.dao.ReferentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Service pour la gestion des entités Referent.
 *
 * Fournit des méthodes CRUD pour manipuler les objets Referent.
 */
@Service
@RequiredArgsConstructor
public class ReferentService {

    // Injection du repository pour l'accès aux données
    private final ReferentRepository referentRepository;

    /**
     * Récupère tous les référents de la base de données.
     *
     * @return une liste de tous les référents.
     */
    public List<Referent> findAll() {
        return referentRepository.findAll();
    }

    /**
     * Recherche un référent par son identifiant.
     *
     * @param id l'identifiant du référent.
     * @return le référent trouvé ou une erreur 404 si non trouvé.
     */
    public Referent findById(Integer id) {
        return referentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Référent non trouvé avec id : " + id));
    }

    /**
     * Sauvegarde ou met à jour un référent dans la base de données.
     *
     * @param referent l'objet Referent à sauvegarder.
     * @return le référent sauvegardé.
     */
    public Referent save(Referent referent) {
        return referentRepository.save(referent);
    }

    /**
     * Met à jour un référent existant.
     *
     * @param id l'identifiant du référent à mettre à jour.
     * @param updated l'objet contenant les nouvelles valeurs.
     * @return le référent mis à jour.
     */
    public Referent updateReferent(Integer id, Referent updated) {
        Referent referent = findById(id);
        referent.setNom(updated.getNom());
        referent.setPrenom(updated.getPrenom());
        referent.setEmail(updated.getEmail());
        return referentRepository.save(referent);
    }

    /**
     * Supprime un référent de la base de données par son identifiant.
     *
     * @param id l'identifiant du référent à supprimer.
     */
    @Transactional
    public void deleteById(Integer id) {
        if (!referentRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Référent non trouvé avec id : " + id);
        }
        referentRepository.deleteById(id);
    }
}