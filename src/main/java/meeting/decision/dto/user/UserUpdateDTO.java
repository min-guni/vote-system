package meeting.decision.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter @AllArgsConstructor
public class UserUpdateDTO {
    private String username;
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }
}
