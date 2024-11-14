package me.thinking_gorilla.learning_axon_framework.repository;

import me.thinking_gorilla.learning_axon_framework.entity.Elephant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElephantRepository extends JpaRepository<Elephant, String> {
}
