package com.github.ku4marez.catalog.service;

import com.github.ku4marez.catalog.dto.ImageViewUrl;
import com.github.ku4marez.catalog.dto.PresignRequest;
import com.github.ku4marez.catalog.dto.PresignResponse;
import com.github.ku4marez.catalog.entity.ProductImageEntity;
import com.github.ku4marez.catalog.repository.ProductImageRepository;
import com.github.ku4marez.catalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final S3PresignService s3;
    private final ProductImageRepository images;
    private final ProductRepository products;

    public PresignResponse presignUpload(String productId, PresignRequest req) {
        products.findById(productId).orElseThrow(() -> new NoSuchElementException("Product not found"));
        String objectKey = "products/%s/%s".formatted(productId, req.filename());
        var presigned = s3.presignPut(objectKey, req.mimeType());

        return new PresignResponse(presigned.url(), objectKey, presigned.expiresAt());
    }

    public String saveMetadata(String productId, String objectKey, String mimeType, Integer width, Integer height, Integer sort) {
        var img = new ProductImageEntity();
        img.setProductId(productId);
        img.setS3Key(objectKey);
        img.setMimeType(mimeType);
        img.setWidth(width);
        img.setHeight(height);
        img.setSort(sort);
        return images.save(img).getId();
    }

    public ImageViewUrl presignView(String objectKey) {
        var link = s3.presignGet(objectKey);
        return new ImageViewUrl(link.url(), link.expiresAt());
    }
}
