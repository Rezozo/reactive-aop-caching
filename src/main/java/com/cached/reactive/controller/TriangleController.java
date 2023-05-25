package com.cached.reactive.controller;

import com.cached.reactive.model.Triangle;
import com.cached.reactive.service.TriangleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class TriangleController {

    private final TriangleService triangleService;

    @RequestMapping(value = "/triangles", method = RequestMethod.GET)
    public Flux<Triangle> allTriangles() {
        return triangleService.getAll();
    }

    @RequestMapping(value = "/triangle/{triangleId}", method = RequestMethod.GET)
    public Mono<Triangle> oneTriangle(@PathVariable Integer triangleId) {
        return triangleService.getById(triangleId);
    }

    @RequestMapping(value = "/triangle", method = RequestMethod.POST)
    public Mono<ResponseEntity<String>> saveTriangle(@RequestBody Triangle triangle) {
        return triangleService.save(triangle)
                .map(triangle1 -> ResponseEntity.ok().build());
    }

    @RequestMapping(value = "/triangle/{triangleId}", method = RequestMethod.DELETE)
    public Mono<Boolean> deleteTriangle(@PathVariable Integer triangleId) {
        return triangleService.deleteById(triangleId);
    }
}
