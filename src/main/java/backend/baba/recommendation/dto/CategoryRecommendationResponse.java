package backend.baba.recommendation.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CategoryRecommendationResponse {
    private String category;
    private List<RecommendationResponse> recommendations;
}
