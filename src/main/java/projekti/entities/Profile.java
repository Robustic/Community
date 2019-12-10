package projekti.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile extends AbstractPersistable<Long> {
    
    private String name;
    private String alias;    
        
    @OneToOne
    private Account account;
    
    @OneToMany(mappedBy = "profile")
    private List<Message> message = new ArrayList<>();
    
    @OneToMany(mappedBy = "follower")
    private List<Following> whoIamFollowing = new ArrayList<>();
    
    @OneToMany(mappedBy = "followed")
    private List<Following> whoAreFollowingMe = new ArrayList<>(); 
    
    @OneToMany(mappedBy = "blocker")
    private List<Blocked> whoIamBlocked = new ArrayList<>();
    
    @OneToMany(mappedBy = "blocked")
    private List<Blocked> whoAreBlockedMe = new ArrayList<>();
    
    @OneToMany(mappedBy = "profile")
    private List<FileObject> fileObjects = new ArrayList<>();
    
    public Profile(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }
}
