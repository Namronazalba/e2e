package e2e.BackendBanking.Service.Transaction;

import e2e.BackendBanking.Dto.Transaction.TransactionResponse;
import e2e.BackendBanking.Dto.Transaction.TransferRequest;
import e2e.BackendBanking.Exception.InvalidAmountException;
import e2e.BackendBanking.Exception.ResourceNotFoundException;
import e2e.BackendBanking.Model.Account.Account;
import e2e.BackendBanking.Model.Transaction.Transaction;
import e2e.BackendBanking.Model.Transaction.TransactionStatus;
import e2e.BackendBanking.Model.Transaction.TransactionType;
import e2e.BackendBanking.Repository.AccountRepository;
import e2e.BackendBanking.Repository.TransactionRepository;
import e2e.BackendBanking.Service.Base.BaseTransactionService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionServiceImpl
        extends BaseTransactionService
        implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository) {

        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }
    @Override
    public void transferOwn(TransferRequest request,
                            Authentication authentication) {

        if (request.getAmount() <= 0) {
            throw new InvalidAmountException("Amount must be greater than 0");
        }

        Account from = accountRepository
                .findByAccountNumber(request.getFromAccountNo())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Source account not found: " + request.getFromAccountNo()
                ));

        Account to = accountRepository
                .findByAccountNumber(request.getToAccountNo())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Destination account not found: " + request.getToAccountNo()
                ));

        validate(from, request.getAmount());
        validate(to, request.getAmount());
        validateBalance(from, request.getAmount());

        from.setBalance(from.getBalance() - request.getAmount());
        to.setBalance(to.getBalance() + request.getAmount());

        accountRepository.save(from);
        accountRepository.save(to);

        saveTransaction(
                transactionRepository,
                TransactionType.TRANSFER,
                TransactionStatus.SUCCESS,
                request.getAmount(),
                from.getId(),
                to.getId(),
                request.getDescription()
        );
    }
    @Override
    public List<TransactionResponse> getAccountTransactions(String accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with id: " + accountId
                        ));

        List<Transaction> transactions =
                transactionRepository.findByFromAccountIdOrToAccountId(
                        accountId,
                        accountId
                );

        return transactions.stream()
                .map(t -> {

                    String fromAccountNumber = null;
                    String toAccountNumber = null;

                    if (t.getFromAccountId() != null) {
                        fromAccountNumber = accountRepository
                                .findById(t.getFromAccountId())
                                .map(Account::getAccountNumber)
                                .orElse(null);
                    }

                    if (t.getToAccountId() != null) {
                        toAccountNumber = accountRepository
                                .findById(t.getToAccountId())
                                .map(Account::getAccountNumber)
                                .orElse(null);
                    }

                    return new TransactionResponse(
                            t.getId(),
                            t.getType(),
                            t.getStatus(),
                            t.getAmount(),
                            t.getTimestamp(),
                            fromAccountNumber,
                            toAccountNumber,
                            account.getBalance(),
                            t.getReference()
                    );
                })
                .toList();
    }
    @Override
    @Transactional
    public void deposit(String accountId, double amount) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with id: " + accountId));

        validate(account, amount);

        account.deposit(amount);

        accountRepository.save(account);

        saveTransaction(
                transactionRepository,
                TransactionType.DEPOSIT,
                TransactionStatus.SUCCESS,
                amount,
                null,
                account.getId(),
                null
        );
    }
    @Override
    @Transactional
    public void withdraw(String accountId, double amount) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with id: " + accountId));

        validate(account, amount);
        validateBalance(account, amount);

        account.debit(amount);

        accountRepository.save(account);

        saveTransaction(
                transactionRepository,
                TransactionType.WITHDRAW,
                TransactionStatus.SUCCESS,
                amount,
                account.getId(),
                null,
                null
        );
    }
    @Override
    public double getBalance(String accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with id: " + accountId));

        return account.getBalance();
    }

}