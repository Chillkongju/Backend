package backend.baba.friend.domain;

import backend.baba.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Friend {

    public enum FriendStatus {
        ACCEPTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member toMember;

    @Enumerated(EnumType.STRING)
    private FriendStatus status;
}