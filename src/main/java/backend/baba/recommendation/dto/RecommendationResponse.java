package backend.baba.recommendation.dto;

import backend.baba.recommendation.domain.Recommendation;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecommendationResponse {

    private String title;
    private String releaseDate;
    private String genre;
    private String summary;

    public static RecommendationResponse from(Recommendation recommendation) {
        return RecommendationResponse.builder()
                .title(recommendation.getTitle())
                .releaseDate(recommendation.getReleaseDate())
                .genre(recommendation.getGenre())
                .summary(recommendation.getSummary())
                .build();
    }
}
