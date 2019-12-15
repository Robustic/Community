package projekti.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.entities.FileObject;
import projekti.entities.FileObjectLike;
import projekti.entities.Profile;

public interface FileObjectLikeRepository extends JpaRepository<FileObjectLike, Long> {
    List<FileObjectLike> findByProfile(Profile profile);
    List<FileObjectLike> findByFileObject(FileObject fileObject);
}
