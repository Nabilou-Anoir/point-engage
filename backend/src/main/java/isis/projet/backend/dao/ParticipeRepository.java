package isis.projet.backend.dao;

import isis.projet.backend.entity.Participe;
import isis.projet.backend.entity.ParticipeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipeRepository extends JpaRepository<Participe, ParticipeKey> {
}