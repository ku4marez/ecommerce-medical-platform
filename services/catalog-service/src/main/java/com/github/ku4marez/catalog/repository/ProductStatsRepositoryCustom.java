package com.github.ku4marez.catalog.repository;

import java.time.Instant;

public interface ProductStatsRepositoryCustom {
    void upsertCounters(String productId, long qty, Instant when);

}
