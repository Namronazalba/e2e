package e2e.BackendBanking.Controller;

import e2e.BackendBanking.Dto.Transaction.AmountRequest;
import e2e.BackendBanking.Dto.Transaction.TransactionResponse;
import e2e.BackendBanking.Dto.Transaction.TransferRequest;
import e2e.BackendBanking.Model.Account.Account;
import e2e.BackendBanking.Service.Account.AccountServiceImpl;
import e2e.BackendBanking.Service.Transaction.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionsController {

    private final TransactionService transactionService;
    private final AccountServiceImpl accountService;

    public TransactionsController(TransactionService transactionService,
                                  AccountServiceImpl accountService) {

        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    // ================= HISTORY =================
    @GetMapping("/history/{accountId}")
    public List<TransactionResponse> getHistory(@PathVariable String accountId) {

//        Account account = accountService.findById(accountId);

        return transactionService.getAccountTransactions(accountId);
    }
    // ================= DEPOSIT =================
    @PostMapping("/deposit/{accountId}")
    public String deposit(@PathVariable String accountId,
                          @RequestBody AmountRequest request,
                          Authentication authentication) {

        if (request.getAmount() <= 0) {
            return "Invalid amount";
        }


        transactionService.deposit(accountId, request.getAmount());

        return "Deposit successful";
    }

    // ================= WITHDRAW =================
    @PostMapping("/withdraw/{accountId}")
    public String withdraw(@PathVariable String accountId,
                           @RequestBody AmountRequest request,
                           Authentication authentication) {

        if (request.getAmount() <= 0) {
            return "Invalid amount";
        }

        transactionService.withdraw(accountId, request.getAmount());

        return "Withdraw successful";
    }

    // ================= BALANCE =================
    @GetMapping("/balance/{accountId}")
    public double getBalance(@PathVariable String accountId,
                             Authentication authentication) {

        Account account = accountService.findById(accountId);

        return transactionService.getBalance(accountId);
    }

    // ================= TRANSFER OWN =================
    @PostMapping("/transfer/own")
    public String transferOwn(@RequestBody TransferRequest request,
                              Authentication authentication) {

        transactionService.transferOwn(request, authentication);

        return "Own transfer successful";
    }



}