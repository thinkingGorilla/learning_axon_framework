package me.thinking_gorilla.learning_axon_framework.events;

import lombok.extern.slf4j.Slf4j;
import me.thinking_gorilla.learning_axon_framework.command.BackToReadyCommand;
import me.thinking_gorilla.learning_axon_framework.command.CreateElephantCommand;
import me.thinking_gorilla.learning_axon_framework.dto.StatusEnum;
import me.thinking_gorilla.learning_axon_framework.entity.Elephant;
import me.thinking_gorilla.learning_axon_framework.repository.ElephantRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class ElephantEventHandler {

    private final EventGateway eventGateway;
    private final ElephantRepository elephantRepository;
    private final CommandGateway commandGateway;

    public ElephantEventHandler(EventGateway eventGateway, ElephantRepository elephantRepository, CommandGateway commandGateway) {
        this.eventGateway = eventGateway;
        this.elephantRepository = elephantRepository;
        this.commandGateway = commandGateway;
    }

    @EventHandler
    private void on(CreateElephantEvent event) {
        log.info("[@EventHandler 'CreatedElephantEvent'] for Id: {}", event.getId());

        // 조회 모델을 관리하기 위해 객체를 생성한다.
        Elephant elephant = new Elephant();
        elephant.setId(event.getId());
        elephant.setName(event.getName());
        elephant.setWeight(event.getWeight());
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

            if (elephant.getWeight() > 100) {
                log.info("==== 100kg 넘어서 넣기 실패! 실패 이벤트 발송!");
                eventGateway.publish(new FailedElephantEvent(event.getId()));
                return;
            }

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

    @EventHandler
    private void on(FailedElephantEvent event) {
        log.info("[@EventHandler 'FailedElephantEvent'] for Id: {}", event.getId());

        // 보상처리 요청
        commandGateway.send(BackToReadyCommand.builder()
                .id(event.getId())
                .status(StatusEnum.READY.value())
                .build()
        );
    }

    @EventHandler
    private void on(BackToReadyCompletedEvent event) {
        log.info("[@EventHandler 'BackToReadyCompletedEvent'] for Id: {}", event.getId());

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
