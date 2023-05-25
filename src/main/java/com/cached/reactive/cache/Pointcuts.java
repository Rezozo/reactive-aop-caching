package com.cached.reactive.cache;

import com.cached.reactive.cache.annotations.LocalCacheClear;
import com.cached.reactive.cache.annotations.LocalCacheable;
import com.cached.reactive.cache.annotations.LocalCacheableAll;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
public class Pointcuts {
    @Pointcut("@annotation(target)")
    public void localCacheable(LocalCacheable target) { }

    @Pointcut("@annotation(target)")
    public void localCacheableAll(LocalCacheableAll target) { }

    @Pointcut("@annotation(target)")
    public void localCacheClear(LocalCacheClear target) { }
}
