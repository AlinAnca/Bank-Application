package com.bank.application.scheduler;

import com.bank.application.model.Authentication;
import com.bank.application.repository.AuthenticationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Configuration
@Component
@Transactional
@EnableScheduling
public class DeleteExpiredTokensScheduler {
    private final AuthenticationRepository authenticationRepository;
    private Logger LOGGER = LoggerFactory.getLogger(DeleteExpiredTokensScheduler.class);

    @Value("${jobs.cronSchedule.expire.time.in_minutes:30}")
    private long value;

    @Autowired
    public DeleteExpiredTokensScheduler(final AuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    @Scheduled(cron = "${jobs.cronSchedule:}")
    public void deleteExpiredTokens() {
        final LocalDateTime now = LocalDateTime.now();
        List<Authentication> authentications = authenticationRepository.findAll();
        for (Authentication auth : authentications) {
            long diff = ChronoUnit.MINUTES.between(auth.getCreatedTime(), now);
            LOGGER.info("Difference in minutes: " + diff);
            LOGGER.info("Expire time" + value);
            if (diff >= value) {
                authenticationRepository.deleteAuthenticationByToken(auth.getToken());
                LOGGER.debug("Token expired!");
                LOGGER.info("Token expired!");
            }
        }
    }
}
