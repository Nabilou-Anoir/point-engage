package isis.projet.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "utilisateur")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "username", nullable = false, unique = true)
    @NotNull(message = "Le username est obligatoire")
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "nom")
    private String nom;

    @Column(name = "promotion")
    private String promotion;

    @Column(name = "prenom")
    private String prenom;


    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}