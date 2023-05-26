package com.cached.reactive.memcached.client;

import com.cached.reactive.model.Triangle;
import net.spy.memcached.MemcachedClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;


public class ReactiveMemClient extends MemcachedClient {
    public ReactiveMemClient(InetSocketAddress... ia) throws IOException {
        super(ia);
    }

    public Mono<Triangle> getTriangle(String key) {
        return Mono.justOrEmpty(super.get(key))
                .map(value -> (Triangle) value)
                .switchIfEmpty(Mono.empty());
    }

    public Flux<Triangle> getAllTriangles(String key) {
        List<Triangle> triangles = (List<Triangle>) super.get(key);

        if (triangles != null)
            return Flux.fromIterable(triangles);
        else
            return Flux.empty();
    }

    public <T> Mono<Boolean> setKeyValue(String key, int exp, T o) {
        return Mono.just(super.set(key, exp, o)).hasElement();
    }


    public Mono<Boolean> deleteKey(String key) {
        return Mono.just(super.delete(key)).hasElement();
    }
}
