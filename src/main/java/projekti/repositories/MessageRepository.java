package projekti.repositories;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.entities.Message;
import projekti.entities.Profile;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByProfileIn(List<Profile> profiles, Pageable pageable);
}

