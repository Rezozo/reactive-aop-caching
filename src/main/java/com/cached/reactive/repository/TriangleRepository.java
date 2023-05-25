package com.cached.reactive.repository;

import com.cached.reactive.model.Triangle;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriangleRepository extends ReactiveMongoRepository<Triangle, Integer> {

}
