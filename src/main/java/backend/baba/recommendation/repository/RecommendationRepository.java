package backend.baba.recommendation.repository;

import backend.baba.recommendation.domain.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<Recommendation,Long> {
    List<Recommendation> findAllByDiaryId(Long diaryId);
    List<Recommendation> findAllByMemberIdAndType(Long memberId, String type);
    Optional<Recommendation> findByIdAndMemberId(Long id, Long memberId);
}
