package com.github.ku4marez.order.configuration;

import com.github.ku4marez.ecom.starters.mongo.MongoCollectionSpec;
import com.github.ku4marez.ecom.starters.mongo.MongoSchemaProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.Index;

import java.util.List;

@Configuration
public class OrderMongoSchema implements MongoSchemaProvider {

    @Override public List<MongoCollectionSpec> collections() {
        return List.of(orders());
    }

    private MongoCollectionSpec orders() {
        String schema = """
        { "$jsonSchema": { "bsonType":"object",
          "required":["customerId","status","totalAmount","currency","items","creationDate","updatedDate"],
          "properties":{
            "id":{"bsonType":"string"},
            "customerId":{"bsonType":"string"},
            "status":{"enum":["NEW","PENDING_PAYMENT","PAID","FULFILLED","FAILED","CANCELLED"]},
            "totalAmount":{"bsonType":["double","decimal","int","long"]},
            "currency":{"bsonType":"string"},
            "idempotencyKey":{"bsonType":["string","null"]},
            "paymentLinkId":{"bsonType":["string","null"]},
            "items":{
              "bsonType":"array","minItems":1,
              "items":{
                "bsonType":"object",
                "required":["productId","productName","sku","quantity","unitPrice"],
                "properties":{
                  "productId":{"bsonType":"string"},
                  "productName":{"bsonType":"string"},
                  "sku":{"bsonType":"string"},
                  "quantity":{"bsonType":["int","long"]},
                  "unitPrice":{"bsonType":["double","decimal","int","long"]}
                }
              }
            },
            "creationDate":{"bsonType":"date"},
            "updatedDate":{"bsonType":"date"}
        } } }
        """;
        var idx = List.of(
            new Index().on("customerId", Sort.Direction.ASC).on("status", Sort.Direction.ASC)
                .named("ix_orders_customer_status"),
            new Index().on("idempotencyKey", Sort.Direction.ASC).named("ix_orders_idempotency").unique()
        );
        return MongoCollectionSpec.of("orders", schema, idx);
    }
}
