package com.cached.reactive.config;

import com.cached.reactive.memcached.client.ReactiveMemClient;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
public class ReactiveMemCacheConfig {

    @Bean
    @SneakyThrows
    public ReactiveMemClient reactiveMemClient() {
        return new ReactiveMemClient(new InetSocketAddress("127.0.0.1", 11211));
    }

}
