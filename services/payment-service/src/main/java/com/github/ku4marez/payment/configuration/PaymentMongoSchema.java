package com.github.ku4marez.payment.configuration;

import com.github.ku4marez.ecom.starters.mongo.MongoCollectionSpec;
import com.github.ku4marez.ecom.starters.mongo.MongoSchemaProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.Index;

import java.util.List;

@Configuration
public class PaymentMongoSchema implements MongoSchemaProvider {

    @Override public List<MongoCollectionSpec> collections() {
        return List.of(paymentLinks(), paymentRefunds());
    }

    private MongoCollectionSpec paymentLinks() {
        String schema = """
        { "$jsonSchema": { "bsonType":"object",
          "required":["orderId","provider","status","creationDate","updatedDate"],
          "properties":{
            "id":{"bsonType":"string"},
            "orderId":{"bsonType":"string"},
            "provider":{"enum":["STRIPE"]},
            "providerRef":{"bsonType":["string","null"]},
            "status":{"enum":["PENDING","SUCCEEDED","FAILED"]},
            "checkoutUrl":{"bsonType":["string","null"]},
            "creationDate":{"bsonType":"date"},
            "updatedDate":{"bsonType":"date"}
        } } }
        """;
        var idx = List.of(
            new Index().on("orderId", Sort.Direction.ASC).named("ix_payment_links_order"),
            new Index().on("provider", Sort.Direction.ASC).on("providerRef", Sort.Direction.ASC)
                .named("ix_payment_links_provider_ref").unique()
        );
        return MongoCollectionSpec.of("payment_links", schema, idx);
    }

    private MongoCollectionSpec paymentRefunds() {
        String schema = """
        { "$jsonSchema": { "bsonType":"object",
          "required":["paymentId","provider","providerRefundRef","status","amount","currency","creationDate","updatedDate"],
          "properties":{
            "id":{"bsonType":"string"},
            "paymentId":{"bsonType":"string"},
            "provider":{"enum":["STRIPE"]},
            "providerRefundRef":{"bsonType":"string"},
            "status":{"enum":["PENDING","SUCCEEDED","FAILED","CANCELED"]},
            "amount":{"bsonType":["double","decimal","int","long"]},
            "currency":{"bsonType":"string"},
            "reason":{"bsonType":["string","null"]},
            "creationDate":{"bsonType":"date"},
            "updatedDate":{"bsonType":"date"}
        } } }
        """;
        var idx = List.of(
            new Index().on("paymentId", Sort.Direction.ASC).named("ix_refunds_payment"),
            new Index().on("providerRefundRef", Sort.Direction.ASC).named("ix_refunds_provider_ref").unique()
        );
        return MongoCollectionSpec.of("payment_refunds", schema, idx);
    }
}
