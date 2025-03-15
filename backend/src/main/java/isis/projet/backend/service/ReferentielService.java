package isis.projet.backend.service;

import isis.projet.backend.entity.Referentiel;
import isis.projet.backend.dao.ReferentielRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service pour la gestion des entités Referentiel.
 *
 * Fournit des méthodes CRUD pour manipuler les objets Referentiel.
 */
@Service
@RequiredArgsConstructor
public class ReferentielService {

    // Injection du repository pour l'accès aux données
    private final ReferentielRepository referentielRepository;

    /**
     * Récupère tous les référentiels de la base de données.
     *
     * @return une liste de tous les référentiels.
     */
    public List<Referentiel> findAll() {
        return referentielRepository.findAll();
    }

    /**
     * Recherche un référentiel par son identifiant.
     *
     * @param id l'identifiant du référentiel.
     * @return le référentiel trouvé ou une erreur 404 si non trouvé.
     */
    public Referentiel findById(Integer id) {
        return referentielRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Référentiel non trouvé avec id : " + id));
    }

    /**
     * Sauvegarde ou met à jour un référentiel dans la base de données.
     *
     * @param referentiel l'objet Referentiel à sauvegarder.
     * @return le référentiel sauvegardé.
     */
    public Referentiel save(Referentiel referentiel) {
        return referentielRepository.save(referentiel);
    }

    /**
     * Met à jour un référentiel existant.
     *
     * @param id l'identifiant du référentiel à mettre à jour.
     * @param updated l'objet contenant les nouvelles valeurs.
     * @return le référentiel mis à jour.
     */
    public Referentiel updateReferentiel(Integer id, Referentiel updated) {
        return referentielRepository.findById(id)
                .map(referentiel -> {
                    referentiel.setNom(updated.getNom());
                    referentiel.setDescription(updated.getDescription());
                    return referentielRepository.save(referentiel);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Référentiel non trouvé avec id : " + id));
    }

    /**
     * Supprime un référentiel de la base de données par son identifiant.
     *
     * @param id l'identifiant du référentiel à supprimer.
     */
    public void deleteById(Integer id) {
        if (!referentielRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Référentiel non trouvé avec id : " + id);
        }
        referentielRepository.deleteById(id);
    }
}