package meeting.decision.dto.user;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String username;
    private String hashedPassword;
}
