package e2e.BackendBanking.Service.User;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginRateLimiterService {

    private static final int MAX_IP_ATTEMPTS = 10;
    private static final int MAX_USER_ATTEMPTS = 5;
    private static final long WINDOW_MS = 60_000; // 1 minute

    private static class AttemptInfo {
        int count;
        long startTime;
    }

    private final ConcurrentHashMap<String, AttemptInfo> ipCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AttemptInfo> userCache = new ConcurrentHashMap<>();

    public void check(String ip, String username) {
        checkLimit(ipCache, "ip:" + ip, MAX_IP_ATTEMPTS);
        checkLimit(userCache, "user:" + username, MAX_USER_ATTEMPTS);
    }

    private void checkLimit(ConcurrentHashMap<String, AttemptInfo> cache,
                            String key,
                            int limit) {

        long now = Instant.now().toEpochMilli();

        AttemptInfo info = cache.computeIfAbsent(key, k -> {
            AttemptInfo a = new AttemptInfo();
            a.startTime = now;
            a.count = 0;
            return a;
        });

        synchronized (info) {

            // reset window
            if (now - info.startTime > WINDOW_MS) {
                info.startTime = now;
                info.count = 0;
            }

            info.count++;

            if (info.count > limit) {
                throw new RuntimeException(
                        "Too many login attempts for key: " + key
                );
            }
        }
    }

}