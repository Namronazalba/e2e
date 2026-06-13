package e2e.BackendBanking.Config;

import e2e.BackendBanking.Model.Transaction.Transaction;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransactionListener extends AbstractMongoEventListener<Transaction> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Transaction> event) {

        Transaction tx = event.getSource();

        if (tx.getReference() == null || tx.getReference().isEmpty()) {
            tx.setReference(generateReference());
        }
    }

    private String generateReference() {
        return "TXN-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 12)
                        .toUpperCase();
    }
}