package backend.baba.member.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 아이디

    @Column(nullable = false)
    private String name; // 이름

    @Column(nullable = false)
    private String password; // 비밀번호

    private String profileImageUrl; // 프로필 이미지 URL

    private String bio;             // 자기소개 (소개글)

    private String preference;      // 취향 (ex. 좋아하는 장르)

    private String link;            // 외부 링크 (블로그, SNS 등)

}
