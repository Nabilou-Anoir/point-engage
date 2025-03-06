package isis.projet.backend.entity;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité représentant un référentiel.
 * Un référentiel peut être associé à plusieurs actions.
 */
@Entity
@Table(name = "referentiel")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Referentiel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_referentiel")
    private Integer idReferentiel;

    @Column(name = "nom")
    private String nom;

    @Column(name = "description")
    @Lob
    private String description;

    /**
     * Relation bidirectionnelle :
     * Un référentiel peut avoir plusieurs actions.
     */
    @OneToMany(mappedBy = "referentiel", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "referentiel-actions")
    private List<Action> actions;
}