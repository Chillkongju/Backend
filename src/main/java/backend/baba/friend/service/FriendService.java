package backend.baba.friend.service;

import backend.baba.friend.domain.Friend;
import backend.baba.friend.repository.FriendRepository;
import backend.baba.member.domain.Member;
import backend.baba.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;

    public void sendFriendRequest(String fromUsername, String toUsername) {
        Member fromUser = memberRepository.findByUsername(fromUsername)
                .orElseThrow(() -> new IllegalArgumentException("보내는 유저가 존재하지 않습니다."));
        Member toUser = memberRepository.findByUsername(toUsername)
                .orElseThrow(() -> new IllegalArgumentException("받는 유저가 존재하지 않습니다."));

        if (friendRepository.findByFromUserAndToUser(fromUser, toUser).isPresent()) {
            throw new IllegalStateException("이미 팔로우한 유저입니다.");
        }

        Friend friendRequest = Friend.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .status(Friend.FriendStatus.ACCEPTED)  // 수정됨: 수락 없이 바로 팔로우 되도록
                .build();

        friendRepository.save(friendRequest);
    }

    // 내가 팔로우한 유저 목록 (내가 fromUser인 경우)
    public List<String> getFollowingList(String username) {
        Member user = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        return friendRepository.findByFromUserAndStatus(user, Friend.FriendStatus.ACCEPTED).stream()
                .map(friend -> friend.getToUser().getUsername())  // toUser가 내가 팔로우한 사람
                .collect(Collectors.toList());
    }

    // 나를 팔로우한 유저 목록 (내가 toUser인 경우)
    public List<String> getFollowerList(String username) {
        Member user = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        return friendRepository.findByToUserAndStatus(user, Friend.FriendStatus.ACCEPTED).stream()
                .map(friend -> friend.getFromUser().getUsername())  // fromUser가 나를 팔로우한 사람
                .collect(Collectors.toList());
    }

    @Transactional
    public void unfollow(String fromUsername, String toUsername) {
        Member fromUser = memberRepository.findByUsername(fromUsername)
                .orElseThrow(() -> new IllegalArgumentException("보내는 유저가 존재하지 않습니다."));
        Member toUser = memberRepository.findByUsername(toUsername)
                .orElseThrow(() -> new IllegalArgumentException("받는 유저가 존재하지 않습니다."));

        Friend friendship = friendRepository.findByFromUserAndToUser(fromUser, toUser)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 관계가 존재하지 않습니다."));

        friendRepository.delete(friendship);
    }
}