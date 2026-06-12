package e2e.BackendBanking.Controller;

import e2e.BackendBanking.Dto.Account.AccountDto;
import e2e.BackendBanking.Model.Account.Account;
import e2e.BackendBanking.Model.User.User;
import e2e.BackendBanking.Service.Account.AccountServiceImpl;
import e2e.BackendBanking.Service.User.UserServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {
    private final AccountServiceImpl accountService;
    private final UserServiceImpl userService;

    public AccountController(AccountServiceImpl accountService,
                             UserServiceImpl userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

//    @PostMapping("/create/{userId}")
//    public Account createAccount(@PathVariable String userId) {
//
//        User user = userService.findById(userId);
//
//        return accountService.createAccount(user);
//    }
    @GetMapping("/me")
    public List<AccountDto> getMyAccounts(Principal principal) {

        String username = principal.getName();

        User user = userService.findByUsername(username);
        System.out.println("USER ID = " + user.getId());
        return accountService.getAccountsByUser(user);
    }
}