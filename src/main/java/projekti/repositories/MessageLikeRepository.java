package projekti.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.entities.MessageLike;
import projekti.entities.Profile;

public interface MessageLikeRepository extends JpaRepository<MessageLike, Long> {
    List<MessageLike> findByProfile(Profile profile);
}
