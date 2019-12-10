package projekti.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekti.entities.FileObject;

public interface FileObjectRepository extends JpaRepository<FileObject, Long> {
    FileObject findByFilename(String filename);  
}
