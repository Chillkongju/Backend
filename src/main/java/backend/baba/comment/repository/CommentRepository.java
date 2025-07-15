package backend.baba.comment.repository;

import backend.baba.comment.domain.Comment;
import backend.baba.diary.domain.Diary;
import backend.baba.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByDiaryOrderByCreatedAtAsc(Diary diary);
    Optional<Comment> findByIdAndMember(Long id, Member member);
}

