package healthcheck.dto.User;

import healthcheck.validation.ValidPassword;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordUserRequest {
    @NotNull
    private String oldPassword;
    @ValidPassword
    private String newPassword;
    private String resetNewPassword;
}