package isis.projet.backend.entity;

import jakarta.persistence.*;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité représentant un étudiant.
 * Un étudiant peut participer à plusieurs actions via Participe.
 */
@Entity
@Table(name = "etudiant")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etudiant")
    private Integer idEtudiant;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "email",nullable = true)
    private String email;

    @Column(name = "promotion")
    private String promotion;

    /**
     * Un étudiant peut avoir plusieurs participations.
     * Ce côté est le "managed" pour la relation etudiant-participations.
     */
    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "etudiant-participations")
    private List<Participe> participations;
}