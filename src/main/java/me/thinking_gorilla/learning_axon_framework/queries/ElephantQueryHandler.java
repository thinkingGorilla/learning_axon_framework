package me.thinking_gorilla.learning_axon_framework.queries;

import lombok.extern.slf4j.Slf4j;
import me.thinking_gorilla.learning_axon_framework.entity.Elephant;
import me.thinking_gorilla.learning_axon_framework.repository.ElephantRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class ElephantQueryHandler {

    private final ElephantRepository elephantRepository;

    public ElephantQueryHandler(ElephantRepository elephantRepository) {
        this.elephantRepository = elephantRepository;
    }

    @QueryHandler
    private Elephant getElephant(GetElephantQuery query) {
        Optional<Elephant> optElephant = elephantRepository.findById(query.getId());
        return optElephant.orElse(null);
    }

    @QueryHandler(queryName = "list")
    private Iterable<Elephant> getElephants(String dummy) {
        Optional<List<Elephant>> optElphants = Optional.of(elephantRepository.findAll());
        return optElphants.orElse(null);
    }
}
