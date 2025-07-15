package backend.baba.recommendation.domain;

import backend.baba.diary.domain.Category;
import backend.baba.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String sourceTitle; // 기반이 된 Diary 작품명

    private String title; // 추천된 작품명

    private String releaseDate; // 개봉일자 또는 연도 (String)

    private String genre;

    @Column(length = 1000)
    private String summary;

    private String type; // "SINGLE" or "MONTHLY"
}
