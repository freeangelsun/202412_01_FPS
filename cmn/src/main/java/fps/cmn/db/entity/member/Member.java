package fps.cmn.db.entity.member;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class Member {
    private int id;
    private String name;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}