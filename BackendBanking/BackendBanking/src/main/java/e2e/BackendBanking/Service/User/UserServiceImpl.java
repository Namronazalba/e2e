package e2e.BackendBanking.Service.User;

import e2e.BackendBanking.Dto.User.AuthResponse;
import e2e.BackendBanking.Dto.User.UserDto;
import e2e.BackendBanking.Mapper.DtoMapper;
import e2e.BackendBanking.Model.User.User;
import e2e.BackendBanking.Repository.UserRepository;
import e2e.BackendBanking.Security.JwtService;
import e2e.BackendBanking.Service.Account.AccountServiceImpl;
import e2e.BackendBanking.Service.Base.BaseUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends BaseUserService
        implements UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AccountServiceImpl accountService;

    public UserServiceImpl(
            UserRepository userRepository,
            JwtService jwtService,
            AccountServiceImpl accountService) {

        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.accountService = accountService;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    @Override
    public UserDto register(
            String firstName,
            String lastName,
            String email,
            String username,
            String phone,
            String address,
            String password,
            String pin) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUsername(username);
        user.setPhone(phone);
        user.setAddress(address);
        user.setPassword(encodePassword(password));
        user.setPin(pin);

        User savedUser = userRepository.save(user);

        accountService.createAccount(savedUser);

        return mapToDto(savedUser);
    }
    private static final Logger log =
            LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public AuthResponse login(
            String username,
            String password) {

        User user = findByUsername(username);

        if (!encoder.matches(password,
                user.getPassword())) {

            throw new RuntimeException("Invalid password");
        }

        user.setLastLogin(LocalDateTime.now());

        userRepository.save(user);

        String accessToken =
                jwtService.generateAccessToken(username);

        String refreshToken =
                jwtService.generateRefreshToken(username);

        return new AuthResponse(
                accessToken,
                refreshToken
        );
    }

    @Override
    public UserDto getCurrentUser(String username) {

        User user = findByUsername(username);

        return DtoMapper.toUserDto(user);
    }
}