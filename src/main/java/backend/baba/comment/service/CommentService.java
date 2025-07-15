package backend.baba.comment.service;

import backend.baba.comment.domain.Comment;
import backend.baba.comment.dto.CommentResponseDto;
import backend.baba.comment.repository.CommentRepository;
import backend.baba.diary.domain.Diary;
import backend.baba.diary.repository.DiaryRepository;
import backend.baba.member.domain.Member;
import backend.baba.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;

    // 댓글 작성
    @Transactional
    public void createComment(String username, Long diaryId, String content) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("기록이 존재하지 않습니다."));

        Comment comment = Comment.builder()
                .content(content)
                .member(member)
                .diary(diary)
                .build();

        commentRepository.save(comment);
        diary.setCommentCount(diary.getCommentCount() + 1);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(String username, Long commentId) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        Comment comment = commentRepository.findByIdAndMember(commentId, member)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않거나 삭제 권한이 없습니다."));

        commentRepository.delete(comment);
        Diary diary = comment.getDiary();
        diary.setCommentCount(Math.max(diary.getCommentCount() - 1, 0));  // 음수 방지
    }

     // 특정 기록(Diary)에 달린 댓글들 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComments(Long diaryId) {

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("기록이 존재하지 않습니다."));

        return commentRepository.findByDiaryOrderByCreatedAtAsc(diary).stream()
                .map(CommentResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}