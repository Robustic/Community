package projekti.repositories;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.entities.FileObject;
import projekti.entities.FileObjectComment;

public interface FileObjectCommentRepository extends JpaRepository<FileObjectComment, Long> {
    List<FileObjectComment> findByFileobject(FileObject fileObject);
    List<FileObjectComment> findByFileobject(FileObject fileObject, Pageable pageable);
}
