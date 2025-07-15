package backend.baba.recommendation.controller;

import backend.baba.diary.domain.Diary;
import backend.baba.diary.repository.DiaryRepository;
import backend.baba.recommendation.dto.CategoryRecommendationResponse;
import backend.baba.recommendation.dto.RecommendationResponse;
import backend.baba.recommendation.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommendations")
@Tag(name = "Recommendation", description = "문화생활 추천 API")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final DiaryRepository diaryRepository;

    @PostMapping("/{diary_id}")
    @Operation(summary = "일일 추천 작품 요청", description = "특정 Diary를 기반으로 GPT가 같은 카테고리 내에서 비슷한 작품 3개를 추천하고 저장합니다.")
    public ResponseEntity<List<RecommendationResponse>> recommendDaily(@PathVariable("diary_id") Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 다이어리를 찾을 수 없습니다: " + diaryId));

        List<RecommendationResponse> recommendations = recommendationService.recommendBySingleDiary(diary);
        return ResponseEntity.ok(recommendations);
    }

    @PostMapping("/monthly/{memberId}")
    @Operation(summary = "이번 달 기록 기반 다음 달 콘텐츠 추천", description = "사용자가 이번 달 작성한 문화생활 기록을 기반으로, 다음 달 추천 콘텐츠를 카테고리별로 제공합니다.")
    public ResponseEntity<List<CategoryRecommendationResponse>> recommendNextMonth(
            @PathVariable Long memberId
    ) {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        List<Diary> diaries = diaryRepository.findByMemberIdAndWatchedAtBetween(memberId, startOfMonth, endOfMonth);

        if (diaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<CategoryRecommendationResponse> response = recommendationService.recommendByMonthlyDiaries(diaries);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{diary_id}")
    @Operation(summary = "일일 문화생활 추천 전체 조회", description = "회원의 일일 추천 콘텐츠 목록을 조회합니다.")
    public ResponseEntity<List<RecommendationResponse>> getRecommendationsByDiary(@PathVariable("diary_id") Long diaryId) {
        List<RecommendationResponse> responses = recommendationService.getRecommendationsByDiaryId(diaryId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/monthly")
    @Operation(summary = "월별 문화생활 추천 전체 조회", description = "회원의 월별 추천 콘텐츠 목록을 카테고리별로 조회합니다.")
    public ResponseEntity<List<CategoryRecommendationResponse>> getMonthlyRecommendations(
            @RequestParam Long memberId
    ) {
        List<CategoryRecommendationResponse> response = recommendationService.getMonthlyRecommendations(memberId);
        return ResponseEntity.ok(response);
    }

}

