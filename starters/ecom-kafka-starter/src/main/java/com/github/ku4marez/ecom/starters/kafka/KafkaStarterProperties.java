package com.github.ku4marez.ecom.starters.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="ecom.kafka")
public class KafkaStarterProperties {
    private boolean enabled = true;
    private boolean autoCreateTopics = true;
    private int partitions = 3;
    private short replicationFactor = 1;
    // Topic names (override per service if you want)
    private String productTopic = "catalog.product.v1";
    private String orderTopic   = "orders.order.v1";
    private String paymentTopic = "payments.payment.v1";
    private String stockTopic   = "inventory.stock.v1";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAutoCreateTopics() {
        return autoCreateTopics;
    }

    public void setAutoCreateTopics(boolean autoCreateTopics) {
        this.autoCreateTopics = autoCreateTopics;
    }

    public int getPartitions() {
        return partitions;
    }

    public void setPartitions(int partitions) {
        this.partitions = partitions;
    }

    public short getReplicationFactor() {
        return replicationFactor;
    }

    public void setReplicationFactor(short replicationFactor) {
        this.replicationFactor = replicationFactor;
    }

    public String getProductTopic() {
        return productTopic;
    }

    public void setProductTopic(String productTopic) {
        this.productTopic = productTopic;
    }

    public String getOrderTopic() {
        return orderTopic;
    }

    public void setOrderTopic(String orderTopic) {
        this.orderTopic = orderTopic;
    }

    public String getPaymentTopic() {
        return paymentTopic;
    }

    public void setPaymentTopic(String paymentTopic) {
        this.paymentTopic = paymentTopic;
    }

    public String getStockTopic() {
        return stockTopic;
    }

    public void setStockTopic(String stockTopic) {
        this.stockTopic = stockTopic;
    }
}
