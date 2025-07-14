package backend.baba.diary.repository;

import backend.baba.diary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary,Long> {

    Optional<Diary> findByIdAndMemberId(Long diaryId, Long memberId);

}
