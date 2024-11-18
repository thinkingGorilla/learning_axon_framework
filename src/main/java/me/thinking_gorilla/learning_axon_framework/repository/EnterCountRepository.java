package me.thinking_gorilla.learning_axon_framework.repository;

import me.thinking_gorilla.learning_axon_framework.entity.EnterCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnterCountRepository extends JpaRepository<EnterCount, String> {
}
