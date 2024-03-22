package meeting.decision.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String username;
    private String hashedPassword;
}
