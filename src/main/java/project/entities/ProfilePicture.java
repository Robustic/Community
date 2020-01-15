package project.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePicture extends AbstractPersistable<Long> {
    
    @OneToOne
    private Profile profile;
    
    @ManyToOne
    private FileObject fileobject;
}
