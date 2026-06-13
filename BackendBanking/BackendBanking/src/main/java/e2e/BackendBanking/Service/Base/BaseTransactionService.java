package e2e.BackendBanking.Service.Base;

import e2e.BackendBanking.Exception.AccountNotFoundException;
import e2e.BackendBanking.Exception.InsufficientBalanceException;
import e2e.BackendBanking.Exception.InvalidAmountException;
import e2e.BackendBanking.Model.Account.Account;
import e2e.BackendBanking.Model.Transaction.Transaction;
import e2e.BackendBanking.Model.Transaction.TransactionStatus;
import e2e.BackendBanking.Model.Transaction.TransactionType;
import e2e.BackendBanking.Repository.TransactionRepository;

import java.util.UUID;


public abstract class BaseTransactionService {

    protected void validate(Account account,
                            double amount) {

        if (account == null) {
            throw new AccountNotFoundException("Account not found");
        }

        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than 0");
        }
    }

    protected void validateBalance(Account account,
                                   double amount) {
        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: " + account.getBalance()
            );
        }
    }


    protected void saveTransaction(
            TransactionRepository transactionRepository,
            TransactionType type,
            TransactionStatus status,
            double amount,
            String from,
            String to,
            String description) {

        Transaction transaction = new Transaction();
        transaction.setReference(generateReference());
        transaction.setType(type);
        transaction.setStatus(status);
        transaction.setAmount(amount);
        transaction.setFromAccount(from);
        transaction.setToAccount(to);
        transaction.setDescription(description);
        transaction.setTimestamp(java.time.LocalDateTime.now());

        transactionRepository.save(transaction);
    }
    protected String generateReference() {
        return "TXN-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

}