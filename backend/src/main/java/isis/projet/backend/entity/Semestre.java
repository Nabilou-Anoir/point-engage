package isis.projet.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Entité représentant un semestre.
 * Un semestre peut regrouper plusieurs participations.
 */
@Entity
@Table(name = "semestre")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Semestre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_semestre")
    private Integer idSemestre;

    @Column(name = "dateDebutSemestre")
    private Date dateDebutSemestre;

    @Column(name = "dateFinSemestre")
    private Date dateFinSemestre;

    @Column(name = "nbSemestre")
    private Integer nbSemestre;

    /**
     * Un semestre peut regrouper plusieurs participations.
     * Ce côté est le "managed" pour la relation semestre-participations.
     */
    @OneToMany(mappedBy = "semestre", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "semestre-participations")
    private List<Participe> participations;
}