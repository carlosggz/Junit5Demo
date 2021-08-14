package com.example.junit5demo.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JokesRepository extends JpaRepository<JokeEntity, Long> {
}
