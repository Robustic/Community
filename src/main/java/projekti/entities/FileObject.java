package projekti.entities;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileObject extends AbstractPersistable<Long> {
    
    private String filename;
    private String contentType;
    private Long contentLength;
    
    @ManyToOne
    private Profile profile;    

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;
}
