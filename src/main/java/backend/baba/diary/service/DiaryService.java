package backend.baba.diary.service;

import backend.baba.diary.domain.Diary;
import backend.baba.diary.dto.request.DiaryCreateRequest;
import backend.baba.diary.dto.response.DiaryResponse;
import backend.baba.diary.repository.DiaryRepository;
import backend.baba.member.domain.Member;
import backend.baba.member.repository.MemberRepository;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;

    // 문화생활 기록
    @Transactional
    public DiaryResponse createDiary(Long id, final DiaryCreateRequest request){
        Member member=memberRepository.findById(id).orElse(null);

        Diary diary=Diary.create(member,request.getTitle(),request.getContent(), request.getCategory(), request.getRating(), request.getWatchedAt());
        diaryRepository.save(diary);
        return new DiaryResponse(diary);
    }

    //문화생활 기록 전체 조회 - 내가 작성한
    public List<DiaryResponse> getAllDiaries(Long id){
        Member member=memberRepository.findById(id).orElse(null);
        List<Diary> diaries=diaryRepository.findAll();
        return diaries.stream().map(DiaryResponse::new).toList();
    }

}
