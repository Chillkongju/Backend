package backend.baba.comment.controller;

import backend.baba.comment.dto.CommentResponseDto;
import backend.baba.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(
            @RequestParam String username,
            @RequestParam Long diaryId,
            @RequestParam String content
    ) {
        commentService.createComment(username, diaryId, content);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteComment(
            @RequestParam String username,
            @RequestParam Long commentId
    ) {
        commentService.deleteComment(username, commentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getComments(@RequestParam Long diaryId) {
        return ResponseEntity.ok(commentService.getComments(diaryId));
    }
}