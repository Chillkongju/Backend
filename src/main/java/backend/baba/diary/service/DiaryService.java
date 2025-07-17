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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;

    // 문화생활 기록
    @Transactional
    public DiaryResponse createDiary(Long id, final DiaryCreateRequest request, MultipartFile imageFile){
        Member member=memberRepository.findById(id).orElse(null);

        String base64Image = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                byte[] imageBytes = imageFile.getBytes();
                base64Image = Base64.getEncoder().encodeToString(imageBytes);
            } catch (IOException e) {
                throw new RuntimeException("이미지 파일을 처리하는 중 오류가 발생했습니다.", e);
            }
        }

        Diary diary=Diary.create(member,request.getTitle(),request.getContent(), request.getCategory(), base64Image,request.getRating(),request.getWatchedAt());
        diaryRepository.save(diary);
        return new DiaryResponse(diary);
    }

    //문화생활 기록 전체 조회 - 내가 작성한
    @Transactional
    public List<DiaryResponse> getAllMyDiaries(Long id){
        List<Diary> diaries=diaryRepository.findAllByMemberId(id);
        return diaries.stream().map(DiaryResponse::new).toList();
    }

    //문화생활기록 - 단일조회
    @Transactional
    public DiaryResponse getDiaryById(Long diaryId, Long memberId){
        Diary diary=diaryRepository.findByIdAndMemberId(diaryId,memberId)
                .orElseThrow(()->new IllegalArgumentException("다이어리가 존재하지 않습니다."));
        return new DiaryResponse(diary);
    }

    public void deleteDiary(Long diaryId, Long memberId){
        Diary diary=diaryRepository.findByIdAndMemberId(diaryId,memberId)
                .orElseThrow(()-> new IllegalArgumentException("기록이 존재하지 않습니다."));
        diaryRepository.delete(diary);
    }

}
