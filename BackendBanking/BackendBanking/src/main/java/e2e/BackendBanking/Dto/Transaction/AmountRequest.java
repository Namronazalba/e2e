package e2e.BackendBanking.Dto.Transaction;


//@Data
public class AmountRequest {
    private double amount;
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
