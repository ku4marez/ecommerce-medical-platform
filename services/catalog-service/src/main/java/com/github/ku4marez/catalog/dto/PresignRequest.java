package com.github.ku4marez.catalog.dto;

import jakarta.validation.constraints.NotBlank;

public record PresignRequest(@NotBlank String filename, @NotBlank String mimeType) {}
