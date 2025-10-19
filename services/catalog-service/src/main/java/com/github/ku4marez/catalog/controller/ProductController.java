package com.github.ku4marez.catalog.controller;

import com.github.ku4marez.catalog.dto.ProductCreateRequest;
import com.github.ku4marez.catalog.dto.ProductOption;
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
    private final ProductService svc;
    private final ProductMapper mapper;

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable String id) {
        return mapper.toResponse(svc.getByIdCached(id));
    }

    @GetMapping("/slug/{slug}")
    public ProductResponse getBySlug(@PathVariable String slug) {
        return mapper.toResponse(svc.getBySlugCached(slug));
    }

    @GetMapping
    public Page<ProductResponse> list(@RequestParam(required = false) String q,
                                      @PageableDefault(size = 20, sort = "creationDate", direction = Sort.Direction.DESC)
                                      Pageable pageable) {
        return svc.search(q, pageable);
    }

    @GetMapping("/options")
    public Page<ProductOption> options(@PageableDefault(size = 50, sort = "name") Pageable pageable) {
        return svc.listOptions(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductCreateRequest r) {
        return mapper.toResponse(svc.create(r));
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable String id, @Valid @RequestBody ProductUpdateRequest r) {
        return mapper.toResponse(svc.update(id, r));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        svc.delete(id);
    }
}

