package com.ktotopawel.opsguard.service;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.ktotopawel.opsguard.config.PubSubConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlertConsumer implements ApplicationRunner {
    private final PubSubTemplate pubSubTemplate;

    @Override
    public void run(ApplicationArguments args) {
        String subscriptionName = PubSubConfig.CRIT_ALERT_SUB;

        log.info("[CONSUMER] Starting listener for the subscription: {}", subscriptionName);

        pubSubTemplate.subscribe(subscriptionName, (message) -> {
            String payload = message.getPubsubMessage().getData().toStringUtf8();
            log.info("[CONSUMER] Received message: {}", payload);
            message.ack();
        });
    }
}
