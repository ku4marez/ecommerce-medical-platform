package com.github.ku4marez.catalog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class S3PresignService {
//    private final S3Client s3;
    private final S3Presigner presigner;
    @Value("${ecom.s3.bucket}")
    private String bucket;
    @Value("${ecom.s3.presign-ttl-seconds:300}")
    private long ttl;

    public record Link(String url, Instant expiresAt) {}

    public Link presignPut(String key, String mime) {
        var put = PutObjectRequest.builder().bucket(bucket).key(key).contentType(mime).build();
        var req = PutObjectPresignRequest.builder().signatureDuration(Duration.ofSeconds(ttl)).putObjectRequest(put).build();
        var url = presigner.presignPutObject(req).url();
        return new Link(url.toString(), Instant.now().plusSeconds(ttl));
    }

    public Link presignGet(String key) {
        var get = GetObjectRequest.builder().bucket(bucket).key(key).build();
        var req = GetObjectPresignRequest.builder().signatureDuration(Duration.ofSeconds(ttl)).getObjectRequest(get).build();
        var url = presigner.presignGetObject(req).url();
        return new Link(url.toString(), Instant.now().plusSeconds(ttl));
    }
}
