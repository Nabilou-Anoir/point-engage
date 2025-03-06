package isis.projet.backend.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité représentant une action.
 * - Peut être validée par un référent.
 * - Appartient à un référentiel.
 * - Peut être liée à plusieurs participations.
 */
@Entity
@Table(name = "action")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_action")
    private Integer idAction;

    @Column(name = "nom")
    private String  nom;

    @Column(name = "dateDebutAction")
    private Date dateDebutAction;

    @Column(name = "dateFinAction")
    private Date dateFinAction;


    @Column(name = "descriptionAction")
    @Lob
    private String descriptionAction;

    /**
     * Plusieurs actions peuvent être validées par un même référent.
     * Ce côté est le "back" de la relation référent-actions.
     */
    @ManyToOne
    @JoinColumn(name = "id_referent")
    @JsonBackReference(value = "referent-actions")
    private Referent referent;

    /**
     * Plusieurs actions peuvent appartenir à un même référentiel.
     * Ce côté est le "back" de la relation référentiel-actions.
     */
    @ManyToOne
    @JoinColumn(name = "id_referentiel")
    @JsonBackReference(value = "referentiel-actions")
    private Referentiel referentiel;

    /**
     * Une action peut avoir plusieurs participations.
     * Ce côté est le "managed" pour la relation action-participations.
     */
    @OneToMany(mappedBy = "action", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "action-participations")
    private List<Participe> participations;
}