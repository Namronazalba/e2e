package e2e.BackendBanking.Model.Account;

import e2e.BackendBanking.Model.Transaction.Transaction;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "accounts")
public class Account {

    @Id
    private String id;

    private String accountNumber;
    private AccountType accountType;
    private Long version;
    private double balance;
    private AccountStatus status;
    private String currency;
    private LocalDateTime createdAt;

    private String userId;

    private List<Transaction> outgoingTransactions;
    private List<Transaction> incomingTransactions;

    protected Account() {}

    public Account(String accountNumber, String userId) {
        this.accountNumber = accountNumber;
        this.userId = userId;

        this.accountType = AccountType.SAVINGS;
        this.status = AccountStatus.ACTIVE;
        this.balance = 0.0;
        this.currency = "PHP";
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }

    public String getUserId() { return userId; }

    public String getAccountNumber() { return accountNumber; }

    public AccountStatus getStatus() { return status; }

    public AccountType getAccountType() { return accountType; }

    public double getBalance() { return balance; }

    public String getCurrency() { return currency; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public Long getVersion() { return version; }

    // setters
    public void setUserId(String userId) { this.userId = userId; }

    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public void setBalance(double balance) { this.balance = balance; }

    public void setStatus(AccountStatus status) { this.status = status; }

    public void setAccountType(AccountType accountType) { this.accountType = accountType; }

    public void setCurrency(String currency) { this.currency = currency; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public void setVersion(Long version) { this.version = version; }

    public void debit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException();
        if (this.balance < amount) throw new IllegalArgumentException("Insufficient balance");

        this.balance -= amount;
    }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Invalid amount");

        this.balance += amount;
    }

}