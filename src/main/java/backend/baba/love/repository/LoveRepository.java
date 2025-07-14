package backend.baba.love.repository;

import backend.baba.love.domain.Love;
import backend.baba.member.domain.Member;
import backend.baba.diary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoveRepository extends JpaRepository<Love, Long> {
    Optional<Love> findByMemberAndDiary(Member member, Diary diary);
    Long countByDiary(Diary diary);
}