package com.github.ku4marez.ecom.starters.mongo;

import java.util.List;

/** Implement this in each service to provide collections to create/validate */
public interface MongoSchemaProvider {
    List<MongoCollectionSpec> collections();
}
