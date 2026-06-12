package e2e.BackendBanking.Service.Transaction;

import e2e.BackendBanking.Dto.Transaction.TransactionResponse;
import e2e.BackendBanking.Dto.Transaction.TransferRequest;
import e2e.BackendBanking.Model.Account.Account;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TransactionService {

    void deposit(String accountId, double amount);

    void withdraw(String accountId, double amount);

    double getBalance(String accountId);

    void transferOwn(
            TransferRequest request,
            Authentication authentication
    );

    List<TransactionResponse> getAccountTransactions(String accountId);
}