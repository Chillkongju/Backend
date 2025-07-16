package backend.baba.diary.dto.response;

import backend.baba.diary.domain.Diary;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DiaryResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String category;
    private final String categoryLabel;
    private final double rating;
    private final String image;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdDate;

    public DiaryResponse(Diary diary){
        this.id=diary.getId();
        this.category=diary.getCategory().name();
        this.categoryLabel=diary.getCategoryLabel();
        this.image= diary.getImage();
        this.title=diary.getTitle();
        this.rating=diary.getRating();
        this.content=diary.getContent();
        this.createdDate = diary.getCreatedDate();
    }
}
