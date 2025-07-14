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

    // 팔로우 요청(언팔 존재 시 다시 팔로우 가능)
    public void sendFriendRequest(String fromUsername, String toUsername) {
        Member fromMember = memberRepository.findByUsername(fromUsername)
                .orElseThrow(() -> new IllegalArgumentException("보내는 유저가 존재하지 않습니다."));
        Member toMember = memberRepository.findByUsername(toUsername)
                .orElseThrow(() -> new IllegalArgumentException("받는 유저가 존재하지 않습니다."));

        // 이미 팔로우 중인지 확인
        if (friendRepository.findByFromMemberAndToMember(fromMember, toMember).isPresent()) {
            throw new IllegalStateException("이미 팔로우 중입니다.");
        }

        Friend friend = Friend.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .status(Friend.FriendStatus.ACCEPTED)
                .build();

        friendRepository.save(friend);
    }

    // 언팔
    @Transactional
    public void unfollow(String fromUsername, String toUsername) {
        Member fromMember = memberRepository.findByUsername(fromUsername)
                .orElseThrow(() -> new IllegalArgumentException("보내는 유저가 존재하지 않습니다."));
        Member toMember = memberRepository.findByUsername(toUsername)
                .orElseThrow(() -> new IllegalArgumentException("받는 유저가 존재하지 않습니다."));

        Friend friend = friendRepository.findByFromMemberAndToMember(fromMember, toMember)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 관계가 존재하지 않습니다."));

        friendRepository.delete(friend);
    }

    // 내가 팔로우한 유저 목록 조회 (팔로잉)
    public List<String> getFollowingList(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        return friendRepository.findByFromMemberAndStatus(member, Friend.FriendStatus.ACCEPTED)
                .stream()
                .map(friend -> friend.getToMember().getUsername())
                .collect(Collectors.toList());
    }

    // 나를 팔로우한 유저 목록 조회 (팔로워)
    public List<String> getFollowerList(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        return friendRepository.findByToMemberAndStatus(member, Friend.FriendStatus.ACCEPTED)
                .stream()
                .map(friend -> friend.getFromMember().getUsername())
                .collect(Collectors.toList());
    }
}