package project.entities;

import java.time.LocalDateTime;
import javax.persistence.Column;
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
public class FileObjectComment extends AbstractPersistable<Long> {
        
    private LocalDateTime localDateTime;

    @Column(columnDefinition = "TEXT")
    private String comment;
    
    @ManyToOne
    private Profile profile;
    
    @ManyToOne
    private FileObject fileobject;
}
