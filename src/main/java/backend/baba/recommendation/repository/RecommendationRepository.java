package backend.baba.recommendation.repository;

import backend.baba.recommendation.domain.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation,Long> {
    List<Recommendation> findAllByDiaryId(Long diaryId);
}
