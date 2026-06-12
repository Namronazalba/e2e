package e2e.BackendBanking.Service.Account;

import e2e.BackendBanking.Dto.Account.AccountDto;
import e2e.BackendBanking.Exception.ResourceNotFoundException;
import e2e.BackendBanking.Mapper.DtoMapper;
import e2e.BackendBanking.Model.Account.Account;
import e2e.BackendBanking.Model.User.User;
import e2e.BackendBanking.Repository.AccountRepository;
import e2e.BackendBanking.Service.Base.BaseAccountService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl extends BaseAccountService
        implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccount(User user) {

        Account account = buildAccount(user);

        return accountRepository.save(account);
    }

    @Override
    public Account findById(Long id) {
        return null;
    }


    @Override
    public Account findById(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
    }

    @Override
    public List<AccountDto> getAccountsByUser(User user) {

        return accountRepository.findByUserId(user.getId())
                .stream()
                .map(DtoMapper::toAccountDto)
                .toList();
    }
}