package projekti.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
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
public class FileObject extends AbstractPersistable<Long> {
    
    private String filename;
    private String contentType;
    private Long contentLength;
    private LocalDateTime localDateTime;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne
    private Profile profile;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;
    
    @OneToMany(mappedBy = "fileobject")
    private List<ProfilePicture> profilePicture = new ArrayList<>();
    
    @OneToMany(mappedBy = "fileobject")
    private List<FileObjectComment> fileObjectComment = new ArrayList<>();
}
