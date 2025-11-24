package com.ktotopawel.opsguard.config;

import com.google.cloud.spring.pubsub.PubSubAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PubSubConfig {

    public static final String CRIT_ALERT_TOPIC = "crit-alert";
    public static final String CRIT_ALERT_SUB = "crit-alert-sub";

    private final PubSubAdmin admin;

    @PostConstruct
    public void init() {
        try {
            if (admin.getTopic(CRIT_ALERT_TOPIC) == null) {
                admin.createTopic(CRIT_ALERT_TOPIC);
                log.info("Created GCP Pub/Sub topic {}", CRIT_ALERT_TOPIC);
            }
            if (admin.getSubscription(CRIT_ALERT_SUB) == null) {
                admin.createSubscription(CRIT_ALERT_SUB, CRIT_ALERT_TOPIC);
                log.info("Created GCP Pub/Sub subscription {}", CRIT_ALERT_SUB);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
