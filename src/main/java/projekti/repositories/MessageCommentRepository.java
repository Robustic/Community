package projekti.repositories;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.entities.Message;
import projekti.entities.MessageComment;

public interface MessageCommentRepository extends JpaRepository<MessageComment, Long> {
    List<MessageComment> findByMessage(Message message, Pageable pageable);
}
