package e2e.BackendBanking.Service.Base;

import e2e.BackendBanking.Dto.User.UserDto;
import e2e.BackendBanking.Model.User.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public abstract class BaseUserService {

    protected final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    protected String encodePassword(String password) {
        return encoder.encode(password);
    }

    protected UserDto mapToDto(User user) {

        return new UserDto(
                user.getId().toString(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                user.getPhone(),
                user.getAddress(),
                user.getCreatedAt(),
                user.getLastLogin(),
                user.getRole()
        );
    }
}