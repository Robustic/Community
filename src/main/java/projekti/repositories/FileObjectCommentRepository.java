package projekti.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekti.entities.FileObjectComment;

public interface FileObjectCommentRepository extends JpaRepository<FileObjectComment, Long> {
    
}
