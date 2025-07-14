package backend.baba.feed.service;


import backend.baba.diary.domain.Diary;
import backend.baba.feed.dto.DiaryResponseDto;
import backend.baba.diary.repository.DiaryRepository;
import backend.baba.friend.domain.Friend;
import backend.baba.friend.repository.FriendRepository;
import backend.baba.member.domain.Member;
import backend.baba.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final DiaryRepository diaryRepository;

    public List<DiaryResponseDto> getFeedByFriend(String username, String category) {
        Member me = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        List<Member> followingList = friendRepository.findByFromMember(me).stream()
                .map(Friend::getToMember)
                .collect(Collectors.toList());

        List<Diary> diaries = diaryRepository.findByMemberIn(followingList);

        return diaries.stream()
                .filter(diary -> category == null || category.equalsIgnoreCase(diary.getCategory().name()))
                .map(DiaryResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}