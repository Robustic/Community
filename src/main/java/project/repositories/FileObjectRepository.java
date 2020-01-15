package project.repositories;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.entities.FileObject;
import project.entities.Profile;

public interface FileObjectRepository extends JpaRepository<FileObject, Long> {
    FileObject findOneById(Long id);
    FileObject findByFilename(String filename);
    FileObject findByFilenameAndProfile(String filename, Profile profile);
    Page<FileObject> findByProfileIn(List<Profile> profiles, Pageable pageable);
    List<FileObject> findByProfile(Profile profiles);
    FileObject findByProfileIsNullAndFilename(String filename);
}
