package projekti.repositories;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.entities.FileObject;
import projekti.entities.Profile;

public interface FileObjectRepository extends JpaRepository<FileObject, Long> {
    FileObject findOneById(Long id);
    FileObject findByFilename(String filename);
    FileObject findByFilenameAndProfile(String filename, Profile profile);
    Page<FileObject> findByProfileIn(List<Profile> profiles, Pageable pageable);
    FileObject findByProfileIsNullAndFilename(String filename);
}
