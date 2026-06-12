package e2e.BackendBanking.Controller;

import e2e.BackendBanking.Dto.User.RegisterRequest;
import e2e.BackendBanking.Dto.User.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import e2e.BackendBanking.Dto.User.LoginRequest;
import e2e.BackendBanking.Dto.User.AuthResponse;
import e2e.BackendBanking.Service.User.UserServiceImpl;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // allow React
public class UsersController {

    private final UserServiceImpl userService;

    public UsersController(UserServiceImpl userService) {
        this.userService = userService;
    }

    // ===== REGISTER =====
    @PostMapping("/register")
    public UserDto register(@RequestBody RegisterRequest request) {

        return userService.register(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getUsername(),
                request.getPhone(),
                request.getAddress(),
                request.getPassword(),
                request.getPin()
        );
    }

    // ===== LOGIN =====
    @PostMapping("/login")
    public AuthResponse login(
            @RequestBody LoginRequest request) {

        return userService.login(
                request.getUsername(),
                request.getPassword()
        );
    }
    @GetMapping("/profile")
    public UserDto getProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return userService.getCurrentUser(username);
    }
}