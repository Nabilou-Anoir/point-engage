package isis.projet.backend.service;

import isis.projet.backend.entity.Semestre;
import isis.projet.backend.dao.SemestreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service pour la gestion des entités Semestre.
 *
 * Fournit des méthodes CRUD pour manipuler les objets Semestre.
 */
@Service
@RequiredArgsConstructor
public class SemestreService {

    // Injection de la dépendance semestreRepository.
    private final SemestreRepository semestreRepository;

    /**
     * Récupère tous les Semestres de la base de données.
     *
     * @return une liste de tous les Semestres.
     */
    public List<Semestre> findAll() {
        return semestreRepository.findAll();
    }

    /**
     * Recherche un Semestre par son identifiant.
     *
     * @param id l'identifiant du Semestre.
     * @return le Semestre trouvé ou une erreur 404 si non trouvé.
     */
    public Semestre findById(Integer id) {
        return semestreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Semestre non trouvé avec id : " + id));
    }

    /**
     * Sauvegarde ou met à jour un Semestre dans la base de données.
     *
     * @param semestre l'objet Semestre à sauvegarder.
     * @return le Semestre sauvegardé.
     */
    public Semestre save(Semestre semestre) {
        return semestreRepository.save(semestre);
    }

    /**
     * Met à jour un Semestre existant.
     *
     * @param id l'identifiant du Semestre à mettre à jour.
     * @param updated l'objet contenant les nouvelles valeurs.
     * @return le Semestre mis à jour.
     */
    public Semestre updateSemestre(Integer id, Semestre updated) {
        return semestreRepository.findById(id)
                .map(semestre -> {
                    semestre.setDateDebutSemestre(updated.getDateDebutSemestre());
                    semestre.setDateFinSemestre(updated.getDateFinSemestre());
                    semestre.setDateDebutDepot(updated.getDateDebutDepot());
                    semestre.setDateFinDepot(updated.getDateFinDepot());
                    semestre.setNbSemestre(updated.getNbSemestre());
                    return semestreRepository.save(semestre);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Semestre non trouvé avec id : " + id));
    }

    /**
     * Supprime un Semestre de la base de données par son identifiant.
     *
     * @param id l'identifiant du Semestre à supprimer.
     */
    public void deleteById(Integer id) {
        if (!semestreRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Semestre non trouvé avec id : " + id);
        }
        semestreRepository.deleteById(id);
    }
}