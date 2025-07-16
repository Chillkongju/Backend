package backend.baba.diary.dto.request;

import backend.baba.diary.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class DiaryCreateRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "카테고리를 입력해주세요.")
    private Category category;

    @NotNull(message = "별점을 입력해주세요.")
    private double rating;

    @NotNull(message = "감상날짜를 입력해주세요.")
    private LocalDate watchedAt;

    public DiaryCreateRequest(String title, String content, Category category, Double rating, LocalDate watchedAt) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.rating = rating;
        this.watchedAt = watchedAt;
    }
}
