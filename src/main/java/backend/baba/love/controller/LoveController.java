package backend.baba.love.controller;

import backend.baba.love.service.LoveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/love")
@RequiredArgsConstructor
public class LoveController {

    private final LoveService loveService;

    // 좋아요 토글 API
    @PostMapping("/toggle")
    public ResponseEntity<String> toggleLove(@RequestParam String username, @RequestParam Long diaryId) {
        boolean liked = loveService.toggleLove(username, diaryId);
        return ResponseEntity.ok(liked ? "좋아요 등록" : "좋아요 해제");
    }

    // 좋아요 여부 조회 API (프론트에서 하트 색 결정용)
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkLove(@RequestParam String username, @RequestParam Long diaryId) {
        return ResponseEntity.ok(loveService.hasUserLoved(username, diaryId));
    }
}