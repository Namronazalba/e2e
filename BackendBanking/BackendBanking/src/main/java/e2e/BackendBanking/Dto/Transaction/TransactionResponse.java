package e2e.BackendBanking.Dto.Transaction;

import e2e.BackendBanking.Model.Transaction.TransactionStatus;
import e2e.BackendBanking.Model.Transaction.TransactionType;

import java.time.LocalDateTime;

public class TransactionResponse {

    private String id;

    private TransactionType type;
    private TransactionStatus status;

    private double amount;
    private LocalDateTime timestamp;

    private String fromAccountNumber;
    private String toAccountNumber;

    private String reference;

    // ===== CONSTRUCTOR =====
    public TransactionResponse(
            String id,
            TransactionType type,
            TransactionStatus status,
            double amount,
            LocalDateTime timestamp,
            String fromAccountNumber,
            String toAccountNumber,
            String reference
    ) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.amount = amount;
        this.timestamp = timestamp;
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.reference = reference;
    }

    // ===== GETTERS =====
    public String getId() { return id; }
    public TransactionType getType() { return type; }
    public TransactionStatus getStatus() { return status; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getFromAccountNumber() { return fromAccountNumber; }
    public String getToAccountNumber() { return toAccountNumber; }
    public String getReference() { return reference; }
}