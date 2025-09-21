package com.github.ku4marez.ecom.starters.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.*;
import org.springframework.util.backoff.FixedBackOff;

@AutoConfiguration
@EnableConfigurationProperties(KafkaStarterProperties.class)
@ConditionalOnClass(KafkaTemplate.class)
@ConditionalOnProperty(prefix="ecom.kafka", name="enabled", havingValue="true", matchIfMissing = true)
public class KafkaAutoConfiguration {

    /** Global error handler with DLT */
    @Bean @ConditionalOnMissingBean
    CommonErrorHandler kafkaErrorHandler(KafkaTemplate<Object, Object> template) {
        var recoverer = new DeadLetterPublishingRecoverer(template);
        return new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 5));
    }

    // Optional: auto-create common topics (guarded by property)
    @Bean @ConditionalOnProperty(prefix="ecom.kafka", name="auto-create-topics", havingValue="true", matchIfMissing=true)
    NewTopic productTopic(KafkaStarterProperties p) {
        return new NewTopic(p.getProductTopic(), p.getPartitions(), p.getReplicationFactor());
    }
    @Bean @ConditionalOnProperty(prefix="ecom.kafka", name="auto-create-topics", havingValue="true", matchIfMissing=true)
    NewTopic orderTopic(KafkaStarterProperties p) {
        return new NewTopic(p.getOrderTopic(), p.getPartitions(), p.getReplicationFactor());
    }
    @Bean @ConditionalOnProperty(prefix="ecom.kafka", name="auto-create-topics", havingValue="true", matchIfMissing=true)
    NewTopic paymentTopic(KafkaStarterProperties p) {
        return new NewTopic(p.getPaymentTopic(), p.getPartitions(), p.getReplicationFactor());
    }
    @Bean @ConditionalOnProperty(prefix="ecom.kafka", name="auto-create-topics", havingValue="true", matchIfMissing=true)
    NewTopic stockTopic(KafkaStarterProperties p) {
        return new NewTopic(p.getStockTopic(), p.getPartitions(), p.getReplicationFactor());
    }
}
