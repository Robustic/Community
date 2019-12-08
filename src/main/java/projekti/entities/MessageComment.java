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
public class MessageComment extends AbstractPersistable<Long> {
        
    private LocalDateTime localDateTime;

    private String comment;
    
    @ManyToOne
    private Message message;
}
