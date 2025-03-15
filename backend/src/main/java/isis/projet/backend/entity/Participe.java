package isis.projet.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Entité "participe" gérant la relation ternaire
 * entre Etudiant, Action et Semestre.
 */
@Entity
@Table(name = "participe")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Participe {

    @EmbeddedId
    private ParticipeKey id;

    /**
     * Relation vers Etudiant.
     * Côté "back" pour etudiant-participations.
     */
    @MapsId("idEtudiant")
    @ManyToOne
    @JoinColumn(name = "id_etudiant", nullable = false)
    @JsonBackReference(value = "etudiant-participations")
    private Etudiant etudiant;

    /**
     * Relation vers Action.
     * Côté "back" pour action-participations.
     */
    @MapsId("idAction")
    @ManyToOne
    @JoinColumn(name = "id_action", nullable = false)
    @JsonBackReference(value = "action-participations")
    private Action action;

    /**
     * Relation vers Semestre.
     * Côté "back" pour semestre-participations.
     */
    @MapsId("idSemestre")
    @ManyToOne
    @JoinColumn(name = "id_semestre", nullable = false)
    @JsonBackReference(value = "semestre-participations")
    private Semestre semestre;

    @Column(name = "totalPoints")
    private BigDecimal totalPoints;

    @Column(name = "nbParticipation")
    private Integer nbParticipation;

    @Column(name = "dateDebutParticipation")
    private Date dateDebutParticipation;

    @Column(name = "dateFinParticipation")
    private Date dateFinParticipation;

    @Lob
    @Column(name = "descriptionParticipation")
    private String descriptionParticipation;

    @Column(name = "statut")
    private Boolean statut;
}