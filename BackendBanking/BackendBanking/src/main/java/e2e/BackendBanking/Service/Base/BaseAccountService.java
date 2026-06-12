package e2e.BackendBanking.Service.Base;

import e2e.BackendBanking.Model.Account.Account;
import e2e.BackendBanking.Model.User.User;

public abstract class BaseAccountService {

    protected String generateAccountNumber() {
        return "ACCT-" + System.currentTimeMillis();
    }

    protected Account buildAccount(User user) {
        return new Account(
                generateAccountNumber(),
                user.getId()
        );
    }

    public abstract Account createAccount(User user);

    public abstract Account findById(String id);
}