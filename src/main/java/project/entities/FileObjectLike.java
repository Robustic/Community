package project.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileObjectLike extends AbstractPersistable<Long> {
    
    @ManyToOne
    private Profile profile;
    
    @ManyToOne
    private FileObject fileObject;
}
