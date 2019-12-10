package projekti.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
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
public class Message extends AbstractPersistable<Long> {
    
    private LocalDateTime localDateTime;

    @Column(columnDefinition = "TEXT")
    private String text;
    
    @ManyToOne
    private Profile profile;
    
    @OneToMany(mappedBy = "message")
    private List<MessageComment> messageComment = new ArrayList<>();
    
}
