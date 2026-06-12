package e2e.BackendBanking.Dto.Account;

import java.time.LocalDateTime;

public class AccountDto {

    private String id;
    private String accountNumber;
    private String accountType;
    private double balance;
    private String status;
    private String currency;
    private LocalDateTime createdAt;

    // constructor
    public AccountDto(String id, String accountNumber, String accountType,
                      double balance, String status,
                      String currency, LocalDateTime createdAt) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.status = status;
        this.currency = currency;
        this.createdAt = createdAt;
    }

    // getters
    public String getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public String getAccountType() { return accountType; }
    public double getBalance() { return balance; }
    public String getStatus() { return status; }
    public String getCurrency() { return currency; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}