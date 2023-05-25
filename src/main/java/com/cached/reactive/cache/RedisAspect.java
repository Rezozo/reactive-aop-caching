package com.cached.reactive.cache;

import com.cached.reactive.cache.annotations.LocalCacheClear;
import com.cached.reactive.cache.annotations.LocalCacheable;
import com.cached.reactive.cache.annotations.LocalCacheableAll;
import com.cached.reactive.model.Triangle;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Aspect
@Component
@AllArgsConstructor
public class RedisAspect {

    private final ReactiveRedisTemplate<String, Triangle> reactiveRedisTemplate;

    @Around("@annotation(localCacheable) && args(id)")
    @SneakyThrows
    public Mono<Triangle> checkCacheById(ProceedingJoinPoint joinPoint, LocalCacheable localCacheable, Integer id) {
        String key = "id_" + id;

        return reactiveRedisTemplate.opsForValue()
                .get(key)
                .switchIfEmpty(setValue(joinPoint, key));
    }

    @SneakyThrows
    private Mono<Triangle> setValue(ProceedingJoinPoint joinPoint, String key) {
        Mono<Triangle> result = (Mono<Triangle>) joinPoint.proceed();

        return result.flatMap(triangle -> reactiveRedisTemplate.opsForValue()
                .set(key, triangle)
                .thenReturn(triangle));
    }

    @Around("@annotation(localCacheableAll)")
    @SneakyThrows
    public Flux<Triangle> checkAllCache(ProceedingJoinPoint joinPoint, LocalCacheableAll localCacheableAll) {
        String key = localCacheableAll.key();

        return reactiveRedisTemplate.opsForSet()
                .members(key)
                .switchIfEmpty(setValueAll(joinPoint, key));
    }

    @SneakyThrows
    private Flux<Triangle> setValueAll(ProceedingJoinPoint joinPoint, String key) {
        Flux<Triangle> result = (Flux<Triangle>) joinPoint.proceed();

        return result.flatMap(triangle -> reactiveRedisTemplate.opsForSet()
                .add(key, triangle)
                .thenReturn(triangle));
    }

    @After("@annotation(localCacheClear) && args(id)")
    private void clearCache(LocalCacheClear localCacheClear, Integer id) {
        reactiveRedisTemplate.delete("id_" + id, "allTriangles").subscribe();
    }
}
