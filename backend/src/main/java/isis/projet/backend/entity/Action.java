package isis.projet.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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
    private String nom;

    @Column(name = "dateDebutAction")
    private Date dateDebutAction;

    @Column(name = "dateFinAction")
    private Date dateFinAction;

    @Lob
    @Column(name = "descriptionAction")
    private String descriptionAction;

    /**
     * Plusieurs actions peuvent être validées par un même référent.
     * Relation "back" vis-à-vis du référent.
     */
    @ManyToOne
    @JoinColumn(name = "id_referent")
    @JsonBackReference(value = "referent-actions")
    private Referent referent;

    /**
     * Plusieurs actions peuvent appartenir à un même référentiel.
     * Relation "back" vis-à-vis du référentiel.
     */
    @ManyToOne
    @JoinColumn(name = "id_referentiel")
    @JsonBackReference(value = "referentiel-actions")
    private Referentiel referentiel;

    /**
     * Une action peut avoir plusieurs participations.
     * Relation "managed" côté action.
     */
    @OneToMany(mappedBy = "action", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "action-participations")
    private List<Participe> participations;
}