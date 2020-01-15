package project.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import project.entities.FileObject;
import project.entities.FileObjectLike;
import project.entities.Profile;

public interface FileObjectLikeRepository extends JpaRepository<FileObjectLike, Long> {
    List<FileObjectLike> findByProfile(Profile profile);
    List<FileObjectLike> findByFileObject(FileObject fileObject);
}
