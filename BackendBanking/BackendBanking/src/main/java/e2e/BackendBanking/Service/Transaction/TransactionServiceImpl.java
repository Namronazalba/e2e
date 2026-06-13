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
import org.springframework.cache.CacheManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class TransactionServiceImpl
        extends BaseTransactionService
        implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final CacheManager cacheManager;
    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository,
            CacheManager cacheManager) {

        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.cacheManager = cacheManager;
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
                        new ResourceNotFoundException("Account not found"));

        // 1. GET LAST SYNC FIRST (IMPORTANT)
        LocalDateTime lastSync = getLastSync(accountId);

        // 2. FETCH NEW TRANSACTIONS FROM DB
        List<Transaction> newTransactions;

        if (lastSync == null) {
            newTransactions = transactionRepository
                    .findByFromAccountIdOrToAccountId(accountId, accountId);
        } else {
            newTransactions = transactionRepository
                    .findByFromAccountIdOrToAccountIdAndTimestampAfter(
                            accountId,
                            accountId,
                            lastSync
                    );
        }

        // 3. CONVERT NEW TRANSACTIONS
        List<TransactionResponse> newResponses =
                mapToResponse(newTransactions, account);

        // 4. GET CACHE (RECENT ONLY, NOT FULL HISTORY)
        List<TransactionResponse> cached = getCachedTransactions(accountId);

        // 5. MERGE (NEW FIRST)
        List<TransactionResponse> merged = new ArrayList<>();
        merged.addAll(newResponses);
        merged.addAll(cached);

        // 6. SORT BY TIMESTAMP (IMPORTANT FIX)
        merged.sort(Comparator.comparing(TransactionResponse::getTimestamp).reversed());

        // 7. KEEP ONLY RECENT 30 ITEMS IN CACHE (IMPORTANT FIX)
        List<TransactionResponse> toCache =
                merged.stream().limit(30).toList();

        // 8. UPDATE CACHE
        updateCache(accountId, toCache);

        // 9. UPDATE SYNC USING MAX TIMESTAMP (NOT now())
        LocalDateTime maxTimestamp = newResponses.stream()
                .map(TransactionResponse::getTimestamp)
                .max(LocalDateTime::compareTo)
                .orElse(lastSync);

        if (maxTimestamp != null) {
            updateLastSync(accountId, maxTimestamp);
        }

        return merged;
    }

    // ---------------- HELPERS ----------------

    private List<TransactionResponse> getCachedTransactions(String accountId) {
        List<TransactionResponse> cache =
                cacheManager.getCache("transactionRecentCache")
                        .get(accountId, List.class);

        return cache != null ? cache : new ArrayList<>();
    }

    private LocalDateTime getLastSync(String accountId) {
        return cacheManager.getCache("transactionLastSyncCache")
                .get(accountId, LocalDateTime.class);
    }

    private void updateCache(String accountId, List<TransactionResponse> data) {
        cacheManager.getCache("transactionRecentCache")
                .put(accountId, data);
    }

    private void updateLastSync(String accountId, LocalDateTime timestamp) {
        cacheManager.getCache("transactionLastSyncCache")
                .put(accountId, timestamp);
    }

    private List<TransactionResponse> mapToResponse(
            List<Transaction> transactions,
            Account account
    ) {
        return transactions.stream()
                .map(t -> new TransactionResponse(
                        t.getId(),
                        t.getType(),
                        t.getStatus(),
                        t.getAmount(),
                        t.getTimestamp(),
                        t.getFromAccountId(),
                        t.getToAccountId(),
                        t.getReference()
                ))
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