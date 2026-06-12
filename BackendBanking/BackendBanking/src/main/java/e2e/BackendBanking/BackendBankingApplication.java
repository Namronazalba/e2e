package e2e.BackendBanking;

import com.mongodb.client.MongoClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendBankingApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(BackendBankingApplication.class, args);
	}
	@Autowired
	MongoClient mongoClient;

	@PostConstruct
	public void debug() {
		System.out.println(mongoClient.getDatabase("banking_db"));
	}

}
