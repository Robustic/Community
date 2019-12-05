package projekti.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Following extends AbstractPersistable<Long> {
    
    private String followerName;
    private String followerAlias;
    private String followedName;
    private String followedAlias;
        
    @ManyToOne
    private Profile follower;
    
    @ManyToOne
    private Profile followed;    
}
