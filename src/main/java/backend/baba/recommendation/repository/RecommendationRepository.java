package backend.baba.recommendation.repository;

import backend.baba.recommendation.domain.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation,Long> {
}
