package isis.projet.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * Entité représentant un référent.
 * Un référent peut valider plusieurs actions.
 */
@Entity
@Table(name = "referent")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Referent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_referent")
    private Integer idReferent;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "referent", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "referent-participations")
    private List<Participe> participationsValidees;
}