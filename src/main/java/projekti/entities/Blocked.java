package projekti.entities;

import java.time.LocalDateTime;
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
public class Blocked extends AbstractPersistable<Long> {
    
    private LocalDateTime localDateTime;
    
    @ManyToOne
    private Profile blocker;
    
    @ManyToOne
    private Profile blocked; 
}
