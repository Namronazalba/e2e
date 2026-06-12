package e2e.BackendBanking.Service.Account;

import e2e.BackendBanking.Dto.Account.AccountDto;
import e2e.BackendBanking.Model.Account.Account;
import e2e.BackendBanking.Model.User.User;

import java.util.List;

public interface AccountService {

    Account createAccount(User user);

    Account findById(Long id);

    List<AccountDto> getAccountsByUser(User user);
}