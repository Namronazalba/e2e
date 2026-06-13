package e2e.BackendBanking.Repository;

import e2e.BackendBanking.Model.Account.Account;
import e2e.BackendBanking.Model.Transaction.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByFromAccountIdOrToAccountId(String fromAccountId,
                                                       String toAccountId);
    List<Transaction> findByFromAccountIdOrToAccountIdAndTimestampAfter(
            String fromId,
            String toId,
            LocalDateTime timestamp
    );
}