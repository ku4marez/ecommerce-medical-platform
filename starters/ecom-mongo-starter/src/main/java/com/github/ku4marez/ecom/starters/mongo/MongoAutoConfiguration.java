package com.github.ku4marez.ecom.starters.mongo;

import jakarta.annotation.PostConstruct;
import org.bson.Document;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

@AutoConfiguration
@EnableMongoAuditing
@ConditionalOnClass(MongoTemplate.class)
public class MongoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    MongoSchemaRunner mongoSchemaRunner(MongoTemplate template, List<MongoSchemaProvider> providers) {
        return new MongoSchemaRunner(template, providers);
    }

    static class MongoSchemaRunner {
        private final MongoTemplate mongo;
        private final List<MongoSchemaProvider> providers;

        MongoSchemaRunner(MongoTemplate mongo, List<MongoSchemaProvider> providers) {
            this.mongo = mongo;
            this.providers = providers;
        }

        @PostConstruct
        public void run() {
            for (var p : providers) {
                for (var spec : p.collections()) {
                    if (!mongo.collectionExists(spec.name())) {
                        mongo.createCollection(spec.name());
                    }
                    // apply validator
                    var cmd = Document.parse("""
            {
              "collMod": "%s",
              "validator": %s,
              "validationLevel": "strict",
              "validationAction": "error"
            }
          """.formatted(spec.name(), spec.jsonSchema()));
                    mongo.executeCommand(cmd);
                    // create indexes (use createIndex, ensureIndex is deprecated)
                    var ops = mongo.indexOps(spec.name());
                    for (var idx : spec.indexes()) {
                        ops.ensureIndex(idx);
                    }
                }
            }
        }
    }
}
