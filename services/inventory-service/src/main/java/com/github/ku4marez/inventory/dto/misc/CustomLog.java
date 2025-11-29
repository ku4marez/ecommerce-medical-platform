package com.github.ku4marez.inventory.dto.misc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomLog {

    static ObjectMapper mapper = new ObjectMapper();

    public static void info(String event, Object... kvPairs) {
        log("INFO", event, kvPairs);
    }

    public static void error(String event, Object... kvPairs) {
        log("ERROR", event, kvPairs);
    }

    private static final Logger log = LoggerFactory.getLogger("CustomLogger");

    private static void log(String level, String event, Object... kvPairs) {
        String traceId = MDC.get("traceId");

        // Build JSON
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("timestamp", Instant.now().toString());
        json.put("traceId", traceId);
        json.put("event", event);

        for (int i = 0; i < kvPairs.length; i += 2) {
            String key = kvPairs[i].toString();
            Object value = kvPairs[i + 1];
            json.put(key, value);
        }

        String line = toJson(json);

        // send to logback
        switch (level) {
            case "INFO" -> log.info(line);
            case "ERROR" -> log.error(line);
            default -> log.debug(line);
        }
    }

    private static String toJson(Map<String, Object> json) {
        try {
            return mapper.writeValueAsString(json);
        } catch (Exception e) {
            return "{}";
        }
    }
}

