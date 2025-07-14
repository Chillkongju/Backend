package backend.baba.love.service;

import backend.baba.diary.domain.Diary;
import backend.baba.diary.repository.DiaryRepository;
import backend.baba.love.domain.Love;
import backend.baba.love.repository.LoveRepository;
import backend.baba.member.domain.Member;
import backend.baba.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoveService {

    private final LoveRepository loveRepository;
    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;

    @Transactional
    public boolean toggleLove(String username, Long diaryId) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("기록 없음"));

        return loveRepository.findByMemberAndDiary(member, diary)
                .map(love -> {
                    loveRepository.delete(love);
                    diary.setLoveCount(diary.getLoveCount() - 1);
                    return false; // 좋아요 해제됨
                })
                .orElseGet(() -> {
                    loveRepository.save(Love.builder().member(member).diary(diary).build());
                    diary.setLoveCount(diary.getLoveCount() + 1);
                    return true; // 좋아요 추가됨
                });
    }

    public boolean hasUserLoved(String username, Long diaryId) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("기록 없음"));

        return loveRepository.findByMemberAndDiary(member, diary).isPresent();
    }
}