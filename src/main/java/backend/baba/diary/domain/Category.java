package backend.baba.diary.domain;

import io.swagger.v3.oas.annotations.media.Schema;

public enum Category {
    @Schema(description = "영화")
    MOVIE("영화"),

    @Schema(description = "도서")
    BOOK("도서"),

    @Schema(description = "공연")
    PERFORMANCE("공연");

    private final String label;

    Category(String label)
    {
        this.label=label;
    }

    public String getLabel(){
        return label;
    }

    public static Category fromLabel(String label) {
        for (Category category : Category.values()) {
            if (category.getLabel().equals(label)) {
                return category;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 카테고리 라벨입니다: " + label);
    }
}
