package com.github.ku4marez.inventory.configuration;

import com.github.ku4marez.ecom.starters.mongo.MongoCollectionSpec;
import com.github.ku4marez.ecom.starters.mongo.MongoSchemaProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.Index;

import java.util.List;
@Configuration
public class InventoryMongoSchema implements MongoSchemaProvider {

    @Override
    public List<MongoCollectionSpec> collections() {
        return List.of(stockItems(), reservations());
    }

    private MongoCollectionSpec stockItems() {
        String schema = """
            { "$jsonSchema": { "bsonType":"object",
              "required":["productId","available","reserved","creationDate","updatedDate"],
              "properties":{
                "id":{"bsonType":"string"},
                "productId":{"bsonType":"string"},
                "available":{"bsonType":["int","long"]},
                "reserved":{"bsonType":["int","long"]},
                "creationDate":{"bsonType":"date"},
                "updatedDate":{"bsonType":"date"}
            } } }
            """;
        var idx = List.of(
            new Index().on("productId", Sort.Direction.ASC).named("ix_stock_product").unique()
        );
        return MongoCollectionSpec.of("stock_items", schema, idx);
    }

    private MongoCollectionSpec reservations() {
        String schema = """
            { "$jsonSchema": { "bsonType":"object",
              "required":["productId","orderId","quantity","status","expiresAt","creationDate","updatedDate"],
              "properties":{
                "id":{"bsonType":"string"},
                "productId":{"bsonType":"string"},
                "orderId":{"bsonType":"string"},
                "quantity":{"bsonType":["int","long"]},
                "status":{"enum":["PENDING","CONFIRMED","RELEASED","EXPIRED"]},
                "expiresAt":{"bsonType":"date"},
                "creationDate":{"bsonType":"date"},
                "updatedDate":{"bsonType":"date"}
            } } }
            """;
        var idx = List.of(
            new Index().on("productId", Sort.Direction.ASC).on("orderId", Sort.Direction.ASC)
                .named("ix_reservation_product_order").unique(),
            new Index().on("status", Sort.Direction.ASC).named("ix_reservation_status"),
            new Index().on("expiresAt", Sort.Direction.ASC).named("ix_reservation_expires_at_ttl").expire(0)
        );
        return MongoCollectionSpec.of("reservations", schema, idx);
    }
}
