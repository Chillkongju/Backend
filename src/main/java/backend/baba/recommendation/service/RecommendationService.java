package backend.baba.recommendation.service;

import backend.baba.diary.domain.Diary;
import backend.baba.gpt.service.GptService;
import backend.baba.recommendation.domain.Recommendation;
import backend.baba.recommendation.dto.RecommendationResponse;
import backend.baba.recommendation.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final GptService gptService;
    private final RecommendationRepository recommendationRepository;

    public List<RecommendationResponse> recommendBySingleDiary(Diary diary) {
        String prompt = String.format(
                "사용자가 최근 감상한 %s 장르의 콘텐츠는 '%s'입니다. " +
                        "비슷한 분위기의 %s 콘텐츠를 3개 추천해주세요. 각 작품에 대해 다음 정보를 포함해주세요:\n" +
                        "- 제목\n" +
                        "- 개봉일자\n" +
                        "- 장르\n" +
                        "- 줄거리 요약\n\n" +
                        "형식은 아래와 같이 정리해주세요:\n" +
                        "1. 제목: XXX\n   개봉일자: YYYY-MM-DD\n   장르: XXX\n   줄거리: XXX",
                diary.getCategory().getLabel(), diary.getTitle(), diary.getCategory().getLabel()
        );

        String gptResult = gptService.askToGpt(prompt);
        List<Recommendation> parsed = parseDetailedRecommendations(gptResult, diary);

        List<Recommendation> saved = parsed.stream()
                .map(recommendationRepository::save)
                .toList();

        return saved.stream()
                .map(RecommendationResponse::from)
                .toList();
    }

    private List<Recommendation> parseDetailedRecommendations(String gptResult, Diary diary) {
        String[] blocks = gptResult.split("(?=\\d+\\. 제목:)");

        return Arrays.stream(blocks)
                .map(block -> {
                    String title = extractField(block, "제목:");
                    String releaseDate = extractField(block, "개봉일자:");
                    String genre = extractField(block, "장르:");
                    String summary = extractField(block, "줄거리:");

                    return Recommendation.builder()
                            .member(diary.getMember())
                            .category(diary.getCategory())
                            .sourceTitle(diary.getTitle())
                            .title(title)
                            .releaseDate(releaseDate)
                            .genre(genre)
                            .summary(summary)
                            .type("SINGLE")
                            .build();
                })
                .toList();
    }

    private String extractField(String block, String fieldName) {
        Pattern pattern = Pattern.compile(fieldName + "\\s*(.*)");
        Matcher matcher = pattern.matcher(block);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }
}
