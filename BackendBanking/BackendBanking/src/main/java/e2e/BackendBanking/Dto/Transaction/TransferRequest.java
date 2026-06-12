package e2e.BackendBanking.Dto.Transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class TransferRequest {

    private String fromAccountId;
    private String toAccountId;
    @NotNull(message = "From account number is required")
    private String fromAccountNo;
    @NotNull(message = "To account number is required")
    private String toAccountNo;
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private double amount;
    private String description;

    // ===== GETTERS & SETTERS =====
    public String getFromAccountId() {
        return fromAccountId;
    }
    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }
    public String getToAccountId() {
        return toAccountId;
    }
    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getFromAccountNo() { return fromAccountNo; }
    public String getToAccountNo() { return toAccountNo; }
    public void setToAccountNo(String toAccountNo) { this.toAccountNo = toAccountNo; }
    public void setFromAccountNo(String fromAccountNo) { this.fromAccountNo = fromAccountNo; }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}