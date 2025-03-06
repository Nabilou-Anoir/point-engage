package isis.projet.backend.dao;
import isis.projet.backend.entity.Referentiel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferentielRepository extends JpaRepository<Referentiel, Integer> {
}