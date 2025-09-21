package com.github.ku4marez.ecom.starters.mongo;

import org.springframework.data.mongodb.core.index.Index;

import java.util.List;

/** Declarative spec per collection */
public record MongoCollectionSpec(String name, String jsonSchema, List<Index> indexes) {
    public static MongoCollectionSpec of(String name, String jsonSchema, List<Index> indexes) {
        return new MongoCollectionSpec(name, jsonSchema, indexes);
    }
}
