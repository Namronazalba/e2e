package e2e.BackendBanking.Model.Transaction;

import e2e.BackendBanking.Model.Account.Account;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;
    private TransactionType type;
    private TransactionStatus status;
    private Currency currency = Currency.PHP;
    private double amount;
    private String reference;
    private double feeAmount = 0.0;
    private double netAmount = 0.0;
    private LocalDateTime timestamp;

    private String description;

    private String fromAccountId;
    private String toAccountId;


    // ===== Constructors =====

    public Transaction() {
        this.timestamp = LocalDateTime.now();
        this.status = TransactionStatus.SUCCESS;
        this.currency = Currency.PHP;
    }

    public Transaction(
            TransactionType type,
            double amount,
            String fromAccount,
            String toAccount) {

        this.type = type;
        this.amount = amount;
        this.fromAccountId = fromAccount;
        this.toAccountId = toAccount;
        this.timestamp = LocalDateTime.now();
        this.status = TransactionStatus.SUCCESS;
        this.currency = Currency.PHP;
        this.netAmount = amount;
    }

    // ===== Getters =====

    public String getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public Currency getCurrency() {
        return currency;
    }

    public double getAmount() {
        return amount;
    }

    public double getFeeAmount() {
        return feeAmount;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }
    public String getReference() {
        return reference;
    }


    // ===== Setters =====

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency == null
                ? Currency.PHP
                : currency;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setFeeAmount(double feeAmount) {
        this.feeAmount = feeAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccountId = fromAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccountId = toAccount;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }
}