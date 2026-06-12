package e2e.BackendBanking.Config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoDebugConfig {

    private final MongoTemplate mongoTemplate;

    public MongoDebugConfig(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void printDb() {
        System.out.println("DB NAME = " + mongoTemplate.getDb().getName());
    }
}