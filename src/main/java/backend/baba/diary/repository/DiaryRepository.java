package backend.baba.diary.repository;
import java.time.LocalDate;
import java.util.List;
import backend.baba.diary.domain.Diary;
import backend.baba.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary,Long> {

    Optional<Diary> findByIdAndMemberId(Long diaryId, Long memberId);

    List<Diary> findByMemberIn(List<Member> members);
    List<Diary> findAllByMemberId(Long memberId);
    List<Diary> findByMemberInOrderByCreatedDateDesc(List<Member> members);
    List<Diary> findByMemberIdAndWatchedAtBetween(Long memberId, LocalDate startDate, LocalDate endDate);

}
