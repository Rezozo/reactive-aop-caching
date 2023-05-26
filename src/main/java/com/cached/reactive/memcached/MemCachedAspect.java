package com.cached.reactive.memcached;

import com.cached.reactive.cache.annotations.LocalCacheClear;
import com.cached.reactive.cache.annotations.LocalCacheable;
import com.cached.reactive.cache.annotations.LocalCacheableAll;
import com.cached.reactive.memcached.client.ReactiveMemClient;
import com.cached.reactive.model.Triangle;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Aspect
@Component
@AllArgsConstructor
public class MemCachedAspect {

    private final ReactiveMemClient reactiveMemClient;

    @Around("@annotation(localCacheable) && args(id)")
    @SneakyThrows
    public Mono<Triangle> checkCacheById(ProceedingJoinPoint joinPoint, LocalCacheable localCacheable, Integer id) {
        String key = "id_" + id;

        return reactiveMemClient
                .getTriangle(key)
                .switchIfEmpty(setValue(joinPoint, key));
    }

    @SneakyThrows
    private Mono<Triangle> setValue(ProceedingJoinPoint joinPoint, String key) {
        Mono<Triangle> result = (Mono<Triangle>) joinPoint.proceed();

        return result.flatMap(triangle -> reactiveMemClient
                .setKeyValue(key, 900, triangle)
                .thenReturn(triangle));
    }

    @Around("@annotation(localCacheableAll)")
    @SneakyThrows
    public Flux<Triangle> checkAllCache(ProceedingJoinPoint joinPoint, LocalCacheableAll localCacheableAll) {
        String key = localCacheableAll.key();

        return reactiveMemClient
                .getAllTriangles(key)
                .switchIfEmpty(setValueAll(joinPoint, key));
    }

    @SneakyThrows
    private Flux<Triangle> setValueAll(ProceedingJoinPoint joinPoint, String key) {
        Flux<Triangle> result = (Flux<Triangle>) joinPoint.proceed();

        return result.collectList()
                .flatMapMany(triangles -> reactiveMemClient
                        .setKeyValue(key, 900, triangles)
                        .thenMany(Flux.fromIterable(triangles)));
    }

    @After("@annotation(localCacheClear) && args(id)")
    private void clearCache(LocalCacheClear localCacheClear, Integer id) {
        reactiveMemClient.deleteKey("id_" + id);
        reactiveMemClient.deleteKey("allTriangles");
    }
}
