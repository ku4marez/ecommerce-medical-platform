package com.github.ku4marez.inventory.mapper.impl;

import com.github.ku4marez.inventory.mapper.ReservationMapper;
import com.github.ku4marez.inventory.mapper.StockItemMapper;

import java.util.HashMap;
import java.util.Map;

public final class Mappers {
    private static final Map<Class<?>, Object> INSTANCES = new HashMap<>();

    static {
        INSTANCES.put(ReservationMapper.class, new ReservationMapperImpl());
        INSTANCES.put(StockItemMapper.class, new StockItemMapperImpl());
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> clazz) {
        Object instance = INSTANCES.get(clazz);
        return clazz.cast(instance);
    }
}
