package isis.projet.backend.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "descriptionAction")
    @Lob
    private String descriptionAction;

    @ManyToOne
    @JoinColumn(name = "id_referentiel")
    @JsonBackReference(value = "referentiel-actions")
    private Referentiel referentiel;

    @OneToMany(mappedBy = "action", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "action-participations")
    private List<Participe> participations;

    // 🔽 AJOUT ICI : pour permettre d’associer un référent
    @ManyToOne
    @JoinColumn(name = "id_referent")
    private Referent referent;
}
