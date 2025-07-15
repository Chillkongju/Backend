package backend.baba.diary.domain;

import backend.baba.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; //작품명

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String content; //감상내용

    @Column(nullable = false)
    private int rating; //별점

    private int loveCount; //좋아요수

    private int commentCount; //댓글수

    private LocalDate watchedAt; //감상 날짜

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate; //작성일시

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Diary(Member member,String title, String content, Category category, int rating, LocalDate watchedAt){
        this.member=member;
        this.title=title;
        this.content=content;
        this.rating=rating;
        this.category=category;
        this.watchedAt=watchedAt;
    }

    public static Diary create(Member member, String title, String content, Category category, int rating, LocalDate watchedAt){
        return new Diary(member, title, content, category,rating, watchedAt);
    }


    public String getCategoryLabel(){
        return this.category.getLabel();
    }

    public void setLoveCount(int loveCount) {
        this.loveCount = loveCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
