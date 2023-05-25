package com.cached.reactive.service.impl;

import com.cached.reactive.cache.annotations.LocalCacheClear;
import com.cached.reactive.cache.annotations.LocalCacheable;
import com.cached.reactive.cache.annotations.LocalCacheableAll;
import com.cached.reactive.model.Triangle;
import com.cached.reactive.repository.TriangleRepository;
import com.cached.reactive.service.TriangleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class TriangleServiceImpl implements TriangleService {

    private final TriangleRepository repository;

    @LocalCacheableAll
    @Override
    public Flux<Triangle> getAll() {
        return repository.findAll();
    }

    @LocalCacheable(key = "{id}")
    @Override
    public Mono<Triangle> getById(Integer id) {
        return repository.findById(id);
    }

    @LocalCacheClear
    @Override
    public Mono<Triangle> save(Triangle triangle) {
        return repository.save(triangle);
    }

    @LocalCacheClear(key = "{id}")
    @Override
    public Mono<Boolean> deleteById(Integer id) {
        return repository.deleteById(id).map(res -> true);
    }
}
