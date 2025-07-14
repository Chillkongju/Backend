package backend.baba.friend.controller;

import backend.baba.friend.domain.Friend;
import backend.baba.friend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    // 친구 요청 시 바로 팔로우로 처리
    @PostMapping("/request")
    public ResponseEntity<String> sendFriendRequest(
            @RequestParam String fromUsername,
            @RequestParam String toUsername
    ) {
        try {
            friendService.sendFriendRequest(fromUsername, toUsername);
            return ResponseEntity.ok("팔로우 완료");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("에러: " + e.getMessage());
        }
    }

    // 팔로우 목록 조회
    @GetMapping("/list")
    public ResponseEntity<?> getFriendList(@RequestParam String username) {
        return ResponseEntity.ok(friendService.getFriendList(username));
    }
}