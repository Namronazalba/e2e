package e2e.BackendBanking.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
@RequiredArgsConstructor
@Service
public class CaptchaService {

    private final RestTemplate restTemplate;

    @Value("${recaptcha.secret}")
    private String secret;


    public boolean verify(String token) {

        String url =
                "https://www.google.com/recaptcha/api/siteverify"
                        + "?secret=" + secret
                        + "&response=" + token;

        Map response =
                restTemplate.postForObject(url, null, Map.class);

        return Boolean.TRUE.equals(response.get("success"));
    }
}
