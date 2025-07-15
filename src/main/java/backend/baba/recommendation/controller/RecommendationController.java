package backend.baba.recommendation.controller;

import backend.baba.diary.domain.Diary;
import backend.baba.diary.repository.DiaryRepository;
import backend.baba.recommendation.dto.RecommendationResponse;
import backend.baba.recommendation.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
