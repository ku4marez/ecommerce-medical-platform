package com.github.ku4marez.catalog.dto;

import java.time.Instant;

public record PresignResponse(String uploadUrl, String objectKey, Instant expiresAt) {}
