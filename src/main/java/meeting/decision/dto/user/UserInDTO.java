package meeting.decision.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class UserInDTO {
    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,15}$", message = "Username must be 6 to 15 characters long")
    private String username;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,15}$", message = "Password must be 6 to 15 characters long")
    private String password;
}
