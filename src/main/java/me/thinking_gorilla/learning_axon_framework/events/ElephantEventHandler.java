package me.thinking_gorilla.learning_axon_framework.events;

import lombok.extern.slf4j.Slf4j;
import me.thinking_gorilla.learning_axon_framework.entity.Elephant;
import me.thinking_gorilla.learning_axon_framework.repository.ElephantRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class ElephantEventHandler {

    private ElephantRepository elephantRepository;

    public ElephantEventHandler(ElephantRepository elephantRepository) {
        this.elephantRepository = elephantRepository;
    }

    @EventHandler
    private void on(CreateElephantEvent event) {
        log.info("[@EventHandler 'CreatedElephantEvent'] for Id: {}", event.getId());

        Elephant elephant = new Elephant();
        elephant.setId(event.getId());
        elephant.setName(event.getName());
        elephant.setStatus(event.getStatus());

        try {
            elephantRepository.save(elephant);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @EventHandler
    private void on(EnteredElephantEvent event) {
        log.info("[@EventHandler 'EnteredElephantEvent'] for Id: {}", event.getId());

        Elephant elephant = getEntity(event.getId());
        if (elephant != null) {
            elephant.setStatus(event.getStatus());
            elephantRepository.save(elephant);
        }
    }

    @EventHandler
    private void on(ExitedElephantEvent event) {
        log.info("[@EventHandler 'ExitedElephantEvent'] for Id: {}", event.getId());

        Elephant elephant = getEntity(event.getId());
        if (elephant != null) {
            elephant.setStatus(event.getStatus());
            elephantRepository.save(elephant);
        }
    }


    private Elephant getEntity(String id) {
        Optional<Elephant> optElephant = elephantRepository.findById(id);
        return optElephant.orElse(null);
    }
}
