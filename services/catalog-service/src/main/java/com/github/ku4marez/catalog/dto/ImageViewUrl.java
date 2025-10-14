package com.github.ku4marez.catalog.dto;

import java.time.Instant;

public record ImageViewUrl(String url, Instant expiresAt) {}

