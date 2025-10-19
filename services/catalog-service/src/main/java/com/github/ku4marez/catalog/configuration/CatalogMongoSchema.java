package com.github.ku4marez.catalog.configuration;

import com.github.ku4marez.ecom.starters.mongo.MongoCollectionSpec;
import com.github.ku4marez.ecom.starters.mongo.MongoSchemaProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.Index;

import java.util.List;

@Configuration
public class CatalogMongoSchema implements MongoSchemaProvider {
    @Override public List<MongoCollectionSpec> collections() {
        return List.of(products(), productImages(), productStats(), eventInbox());
    }

    private MongoCollectionSpec products() {
        String schema = """
        { "$jsonSchema": { "bsonType":"object",
          "required":["sku","slug","name","status","price","currency","creationDate","updatedDate"],
          "properties":{
            "id":{"bsonType":"string"},
            "sku":{"bsonType":"string"},
            "slug":{"bsonType":"string"},
            "name":{"bsonType":"string"},
            "description":{"bsonType":["string","null"]},
            "status":{"enum":["DRAFT","ACTIVE","ARCHIVED"]},
            "price":{"bsonType":["double","decimal","int","long"]},
            "currency":{"bsonType":"string"},
            "categoryId":{"bsonType":["string","null"]},
            "attributes":{"bsonType":["object","null"]},
            "imageIds":{"bsonType":["array","null"],"items":{"bsonType":"string"}},
            "creationDate":{"bsonType":"date"},
            "updatedDate":{"bsonType":"date"}
          } } }
        """;
        var idx = List.of(
            new Index().on("slug", Sort.Direction.ASC).named("ix_products_slug").unique(),
            new Index().on("sku",  Sort.Direction.ASC).named("ix_products_sku").unique(),
            new Index().on("status", Sort.Direction.ASC).named("ix_products_status")
        );
        return MongoCollectionSpec.of("products", schema, idx);
    }

    private MongoCollectionSpec productImages() {
        String schema = """
        { "$jsonSchema": { "bsonType":"object",
          "required":["productId","s3Key","mimeType","creationDate","updatedDate"],
          "properties":{
            "id":{"bsonType":"string"},
            "productId":{"bsonType":"string"},
            "s3Key":{"bsonType":"string"},
            "mimeType":{"bsonType":"string"},
            "width":{"bsonType":["int","long","null"]},
            "height":{"bsonType":["int","long","null"]},
            "sort":{"bsonType":["int","long","null"]},
            "creationDate":{"bsonType":"date"},
            "updatedDate":{"bsonType":"date"}
          } } }
        """;
        var idx = List.of(
            new Index().on("productId", Sort.Direction.ASC)
                .on("sort", Sort.Direction.ASC)
                .named("ix_product_images_product")
        );
        return MongoCollectionSpec.of("product_images", schema, idx);
    }

    // NEW: projection used by the order-event consumer
    private MongoCollectionSpec productStats() {
        String schema = """
        { "$jsonSchema": { "bsonType":"object",
          "required":["productId","ordersCount","unitsSold","creationDate","updatedDate"],
          "properties":{
            "id":{"bsonType":"string"},
            "productId":{"bsonType":"string"},
            "ordersCount":{"bsonType":["int","long"]},
            "unitsSold":{"bsonType":["int","long"]},
            "lastOrderedAt":{"bsonType":["date","null"]},
            "creationDate":{"bsonType":"date"},
            "updatedDate":{"bsonType":"date"}
          } } }
        """;
        var idx = List.of(
            new Index().on("productId", Sort.Direction.ASC)
                .named("ix_product_stats_product").unique(),
            new Index().on("unitsSold", Sort.Direction.DESC)
                .named("ix_product_stats_units_sold")
        );
        return MongoCollectionSpec.of("product_stats", schema, idx);
    }

    private MongoCollectionSpec eventInbox() {
        String schema = """
        { "$jsonSchema": { "bsonType":"object",
          "required":["eventId","eventType","creationDate","updatedDate"],
          "properties":{
            "id":{"bsonType":"string"},
            "eventId":{"bsonType":"string"},
            "eventType":{"bsonType":"string"},
            "creationDate":{"bsonType":"date"},
            "updatedDate":{"bsonType":"date"}
          } } }
        """;
        var idx = List.of(
            new Index().on("eventId", Sort.Direction.ASC)
                .named("ix_event_inbox_eventId").unique()
        );
        return MongoCollectionSpec.of("event_inbox", schema, idx);
    }
}
