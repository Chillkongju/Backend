package backend.baba.recommendation.service;

import backend.baba.diary.domain.Category;
import backend.baba.diary.domain.Diary;
import backend.baba.gpt.service.GptService;
import backend.baba.member.domain.Member;
import backend.baba.recommendation.domain.Recommendation;
import backend.baba.recommendation.dto.CategoryRecommendationResponse;
import backend.baba.recommendation.dto.RecommendationResponse;
import backend.baba.recommendation.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    private String buildMonthlyPrompt(Map<Category, List<String>> titleMap) {
        StringBuilder sb = new StringBuilder("다음은 사용자가 이번 달 감상한 콘텐츠 목록입니다.\n\n");

        for (Map.Entry<Category, List<String>> entry : titleMap.entrySet()) {
            sb.append("- ").append(entry.getKey().getLabel()).append(": ");
            sb.append(String.join(", ", entry.getValue())).append("\n");
        }

        sb.append("""
            각 카테고리별로 **각각 3개씩** 다음 달에 감상하면 좋을 콘텐츠를 추천해주세요.
            각 콘텐츠는 다음 항목을 포함해주세요:
            - 제목
            - 개봉일자 또는 출간일
            - 장르
            - 줄거리
            
            형식은 아래와 같이 정리해주세요:
            [카테고리명]
            1. 제목: XXX
               개봉일자: YYYY-MM-DD
               장르: XXX
               줄거리: XXX
            """);

        return sb.toString();
    }

    private String extractMonthlyField(String block, String fieldName) {
        Pattern pattern = Pattern.compile(fieldName + "\\s*(.*?)(?=\\n\\s*(제목:|개봉일자:|장르:|줄거리:|$))", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(block);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

    public List<CategoryRecommendationResponse> recommendByMonthlyDiaries(List<Diary> diaries) {
        Map<Category, List<String>> titleMap = new HashMap<>();
        for (Diary d : diaries) {
            titleMap.computeIfAbsent(d.getCategory(), k -> new ArrayList<>()).add(d.getTitle());
        }

        String prompt = buildMonthlyPrompt(titleMap);
        String gptResult = gptService.askToGpt(prompt);

        List<Recommendation> saved = parseMonthlyRecommendations(gptResult, diaries.get(0).getMember());

        return saved.stream()
                .collect(Collectors.groupingBy(Recommendation::getCategory))
                .entrySet()
                .stream()
                .map(entry -> CategoryRecommendationResponse.builder()
                        .category(entry.getKey().getLabel())
                        .recommendations(
                                entry.getValue().stream()
                                        .map(RecommendationResponse::from)
                                        .toList()
                        )
                        .build())
                .toList();
    }

    private List<Recommendation> parseMonthlyRecommendations(String gptResult, Member member) {
        List<Recommendation> recommendations = new ArrayList<>();

        Pattern blockPattern = Pattern.compile("\\[(.*?)\\](.*?)((?=\\[)|$)", Pattern.DOTALL);
        Matcher matcher = blockPattern.matcher(gptResult);

        while (matcher.find()) {
            String categoryLabel = matcher.group(1).trim();
            String block = matcher.group(2);

            Category category = Category.fromLabel(categoryLabel);
            String[] items = block.split("(?=\\d+\\. 제목:)");

            for (String item : items) {
                String title = extractMonthlyField(item, "제목:");
                String releaseDate = extractMonthlyField(item, "개봉일자:");
                String genre = extractMonthlyField(item, "장르:");
                String summary = extractMonthlyField(item, "줄거리:");

                recommendations.add(Recommendation.builder()
                        .member(member)
                        .category(category)
                        .title(title)
                        .releaseDate(releaseDate)
                        .genre(genre)
                        .summary(summary)
                        .type("MONTHLY")
                        .build());
            }
        }

        return recommendations.stream()
                .map(recommendationRepository::save)
                .toList();
    }



}
