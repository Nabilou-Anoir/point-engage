package isis.projet.backend.dao;
import isis.projet.backend.entity.Referent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferentRepository extends JpaRepository<Referent, Integer> {
}