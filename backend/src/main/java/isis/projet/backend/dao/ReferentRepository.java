package isis.projet.backend.dao;
import isis.projet.backend.entity.Referent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReferentRepository extends JpaRepository<Referent, Integer> {
    Optional<Referent> findByEmail(String email);
}