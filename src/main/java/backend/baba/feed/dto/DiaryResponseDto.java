package backend.baba.feed.dto;

import backend.baba.diary.domain.Diary;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DiaryResponseDto {

    private Long diaryId;
    private String username;
    private String profileImageUrl;
    private String title;
    private String content;
    private String category;
    private LocalDateTime createdAt;

    public static DiaryResponseDto fromEntity(Diary diary) {
        return DiaryResponseDto.builder()
                .diaryId(diary.getId())
                .username(diary.getMember().getUsername())
                .profileImageUrl(diary.getMember().getProfileImageUrl())
                .title(diary.getTitle())
                .content(diary.getContent())
                .category(diary.getCategory().name())
                .createdAt(diary.getCreatedDate())
                .build();
    }
}