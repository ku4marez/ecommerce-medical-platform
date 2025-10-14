package com.github.ku4marez.catalog.controller;

import com.github.ku4marez.catalog.dto.ImageViewUrl;
import com.github.ku4marez.catalog.dto.PresignRequest;
import com.github.ku4marez.catalog.dto.PresignResponse;
import com.github.ku4marez.catalog.service.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/products/{productId}/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageSvc;

    @PostMapping("/presign")
    public PresignResponse presign(@PathVariable String productId, @Valid @RequestBody PresignRequest req) {
        return imageSvc.presignUpload(productId, req);
    }

    // after client uploads to S3 with the PUT URL, it calls this to persist metadata
    @PostMapping("/metadata")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String,String> save(@PathVariable String productId,
                                   @RequestParam String key,
                                   @RequestParam String mimeType,
                                   @RequestParam(required = false) Integer width,
                                   @RequestParam(required = false) Integer height,
                                   @RequestParam(required = false) Integer sort) {
        String id = imageSvc.saveMetadata(productId, key, mimeType, width, height, sort);
        return Map.of("id", id);
    }

    @GetMapping("/view-url")
    public ImageViewUrl view(@RequestParam String key) {
        return imageSvc.presignView(key);
    }
}
