package backend.baba.feed.controller;

import backend.baba.feed.dto.DiaryResponseDto;
import backend.baba.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    /**
     * 팔로잉 중인 사용자의 기록(Diary) 목록 조회 API
     * @param username 현재 로그인한 유저의 username
     * @param category (Optional) 필터링할 카테고리 (예: BOOK, MOVIE, PERFORMANCE)
     * @return 팔로잉 중인 유저들의 Diary 목록
     */
    @GetMapping
    public ResponseEntity<List<DiaryResponseDto>> getFeed(
            @RequestParam String username,
            @RequestParam(required = false) String category
    ) {
        List<DiaryResponseDto> feed = feedService.getFeedByFriend(username, category);
        return ResponseEntity.ok(feed);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DiaryResponseDto>> getAllFeed(@RequestParam String username) {
        return ResponseEntity.ok(feedService.getAllFeed(username));
    }
}