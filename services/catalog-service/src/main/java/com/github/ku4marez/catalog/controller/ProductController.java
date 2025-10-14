package com.github.ku4marez.catalog.controller;

import com.github.ku4marez.catalog.dto.ProductCreateRequest;
import com.github.ku4marez.catalog.dto.ProductResponse;
import com.github.ku4marez.catalog.dto.ProductUpdateRequest;
import com.github.ku4marez.catalog.mapper.ProductMapper;
import com.github.ku4marez.catalog.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;
    private final ProductMapper mapper;

    @GetMapping("/{id}")
    public ProductResponse get(@PathVariable String id) {
        return mapper.toResponse(service.getByIdCached(id));
    }

    @GetMapping
    public Page<ProductResponse> list(
        @RequestParam(required = false) String q,
        @PageableDefault(size = 20, sort = "creationDate", direction = Sort.Direction.DESC) Pageable pageable) {
        var page = (q == null || q.isBlank()) ? service.list(pageable) : service.search(q, pageable);
        return page.map(mapper::toResponse);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductCreateRequest req) {
        return mapper.toResponse(service.create(req));
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable String id, @Valid @RequestBody ProductUpdateRequest req) {
        return mapper.toResponse(service.update(id, req));
    }
}
