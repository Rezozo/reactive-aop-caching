package com.cached.reactive.service;

import com.cached.reactive.model.Triangle;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TriangleService {
    Flux<Triangle> getAll();

    Mono<Triangle> getById(Integer id);

    Mono<Triangle> save(Triangle triangle);

    Mono<Boolean> deleteById(Integer id);
}
