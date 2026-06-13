package e2e.BackendBanking.Service.User;

import e2e.BackendBanking.Dto.User.AuthResponse;
import e2e.BackendBanking.Dto.User.UserDto;
import e2e.BackendBanking.Exception.InvalidCredentialsException;
import e2e.BackendBanking.Mapper.DtoMapper;
import e2e.BackendBanking.Model.User.User;
import e2e.BackendBanking.Repository.UserRepository;
import e2e.BackendBanking.Config.CaptchaService;
import e2e.BackendBanking.Security.JwtService;
import e2e.BackendBanking.Exception.AccountLockedException;
import e2e.BackendBanking.Service.Account.AccountServiceImpl;
import e2e.BackendBanking.Service.Base.BaseUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends BaseUserService
        implements UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AccountServiceImpl accountService;
    private final LoginRateLimiterService loginRateLimiterService;
    @Autowired
    private CaptchaService captchaService;

    public UserServiceImpl(
            UserRepository userRepository,
            JwtService jwtService,
            LoginRateLimiterService loginRateLimiterService,
            AccountServiceImpl accountService) {

        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.loginRateLimiterService = loginRateLimiterService;
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
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public AuthResponse login(
            String username,
            String password,
            String captchaToken,
            String ipAddress) {

        // 1. RATE LIMIT CHECK (FIRST LINE)
        loginRateLimiterService.check(ipAddress, username);

        // 2. CAPTCHA VERIFY (SECOND LINE)
        if (!captchaService.verify(captchaToken)) {
            throw new RuntimeException("Invalid captcha");
        }

        // 3. FIND USER
        User user = findByUsername(username);

        // 4. CHECK ACCOUNT LOCK
        if (user.getLockUntil() != null &&
                user.getLockUntil().isAfter(LocalDateTime.now())) {

            throw new AccountLockedException(
                    "Account is locked until " + user.getLockUntil()
            );
        }

        // 5. PASSWORD CHECK
        if (!encoder.matches(password, user.getPassword())) {

            handleFailedLogin(user);

            throw new InvalidCredentialsException();
        }

        // 6. SUCCESS → RESET ATTEMPTS
        resetLoginAttempts(user);

        // 7. GENERATE TOKENS
        String accessToken = jwtService.generateAccessToken(username);
        String refreshToken = jwtService.generateRefreshToken(username);

        return new AuthResponse(accessToken, refreshToken);
    }
    private void resetLoginAttempts(User user) {
        user.setFailedAttempts(0);
        user.setLockUntil(null);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }
    private void handleFailedLogin(User user) {

        int attempts =
                user.getFailedAttempts() == null
                        ? 0
                        : user.getFailedAttempts();

        attempts++;

        user.setFailedAttempts(attempts);

        if (attempts >= 5) {

            user.setLockUntil(
                    LocalDateTime.now().plusMinutes(15)
            );

            userRepository.save(user);

            throw new AccountLockedException(
                    "Too many failed login attempts. Account locked for 15 minutes."
            );
        }

        userRepository.save(user);
    }
    @Override
    public UserDto getCurrentUser(String username) {

        User user = findByUsername(username);

        return DtoMapper.toUserDto(user);
    }
}