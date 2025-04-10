package isis.projet.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * Clé composite pour la table de liaison "participe".
 * (id_etudiant, id_action, id_semestre) forment la clé primaire.
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ParticipeKey implements Serializable {

    @Column(name = "id_etudiant")
    private Integer idEtudiant;

    @Column(name = "id_action")
    private Integer idAction;

    @Column(name = "id_semestre")
    private Integer idSemestre;
}