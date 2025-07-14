package backend.baba.friend.repository;

import backend.baba.friend.domain.Friend;
import backend.baba.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByFromMemberAndToMember(Member fromMember, Member toMember);
    List<Friend> findByFromMemberAndStatus(Member fromMember, Friend.FriendStatus status);
    List<Friend> findByToMemberAndStatus(Member toMember, Friend.FriendStatus status);
    List<Friend> findByFromMember(Member fromMember);
}