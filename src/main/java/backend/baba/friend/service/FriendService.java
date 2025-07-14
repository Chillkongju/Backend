package backend.baba.friend.service;

import backend.baba.friend.domain.Friend;
import backend.baba.friend.repository.FriendRepository;
import backend.baba.member.domain.Member;
import backend.baba.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;

    // 친구 요청 시 바로 팔로우 저장
    public void sendFriendRequest(String fromUsername, String toUsername) {
        Member fromUser = memberRepository.findByUsername(fromUsername)
                .orElseThrow(() -> new IllegalArgumentException("보내는 유저가 존재하지 않습니다."));
        Member toUser = memberRepository.findByUsername(toUsername)
                .orElseThrow(() -> new IllegalArgumentException("받는 유저가 존재하지 않습니다."));

        // 이미 팔로우한 경우 예외 처리
        if (friendRepository.findByFromUserAndToUser(fromUser, toUser).isPresent()) {
            throw new IllegalStateException("이미 팔로우한 사용자입니다.");
        }

        Friend friend = Friend.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .build();  // status 없이 바로 저장

        friendRepository.save(friend);
    }

    // 친구 목록 조회 (status 없이 from 기준)
    public List<Friend> getFriendList(String username) {
        Member user = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        return friendRepository.findByFromUser(user);
    }
}