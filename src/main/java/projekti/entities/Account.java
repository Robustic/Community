package projekti.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractPersistable<Long> {

    private String username;
    private String password;
    
    @OneToOne(mappedBy = "account")
    private Profile profile; 
    
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

