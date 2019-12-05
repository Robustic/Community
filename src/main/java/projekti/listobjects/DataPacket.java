package projekti.listobjects;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataPacket {
    private String name;
    private String alias;
    private LocalDateTime localDateTime;
    private String text;
}
