package backend.baba.friend.repository;

import backend.baba.friend.domain.Friend;
import backend.baba.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    Optional<Friend> findByFromUserAndToUser(Member fromUser, Member toUser);

    List<Friend> findByFromUser(Member fromUser);
}