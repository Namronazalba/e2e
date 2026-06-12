package e2e.BackendBanking.Service.User;

import e2e.BackendBanking.Dto.User.AuthResponse;
import e2e.BackendBanking.Dto.User.UserDto;
import e2e.BackendBanking.Model.User.User;

public interface UserService {

    User findByUsername(String username);

    User findById(String id);

    UserDto register(
            String firstName,
            String lastName,
            String email,
            String username,
            String phone,
            String address,
            String password,
            String pin
    );

    AuthResponse login(String username, String password);

    UserDto getCurrentUser(String username);
}