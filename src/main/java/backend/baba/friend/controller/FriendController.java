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

    // 팔로우 요청 API
    @PostMapping("/request")
    public ResponseEntity<String> sendFriendRequest(@RequestParam String fromUsername, @RequestParam String toUsername) {
        friendService.sendFriendRequest(fromUsername, toUsername);
        return ResponseEntity.ok("팔로우 완료");
    }

    // 내가 팔로우한 유저 목록 조회 API
    @GetMapping("/following")
    public ResponseEntity<List<String>> getFollowing(@RequestParam String username) {
        return ResponseEntity.ok(friendService.getFollowingList(username));
    }

    // 나를 팔로우한 유저 목록 조회 API
    @GetMapping("/follower")
    public ResponseEntity<List<String>> getFollower(@RequestParam String username) {
        return ResponseEntity.ok(friendService.getFollowerList(username));
    }
}