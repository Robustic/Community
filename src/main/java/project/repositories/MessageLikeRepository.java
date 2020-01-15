package project.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import project.entities.MessageLike;
import project.entities.Profile;

public interface MessageLikeRepository extends JpaRepository<MessageLike, Long> {
    List<MessageLike> findByProfile(Profile profile);
}
