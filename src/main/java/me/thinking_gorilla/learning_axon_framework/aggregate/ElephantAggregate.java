package me.thinking_gorilla.learning_axon_framework.aggregate;

import lombok.extern.slf4j.Slf4j;
import me.thinking_gorilla.learning_axon_framework.command.BackToReadyCommand;
import me.thinking_gorilla.learning_axon_framework.command.CreateElephantCommand;
import me.thinking_gorilla.learning_axon_framework.command.EnterElephantCommand;
import me.thinking_gorilla.learning_axon_framework.command.ExitElephantCommand;
import me.thinking_gorilla.learning_axon_framework.events.BackToReadyCompletedEvent;
import me.thinking_gorilla.learning_axon_framework.events.CreateElephantEvent;
import me.thinking_gorilla.learning_axon_framework.events.EnteredElephantEvent;
import me.thinking_gorilla.learning_axon_framework.events.ExitedElephantEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.spring.stereotype.Aggregate;

@Slf4j
@Aggregate
public class ElephantAggregate {
    @AggregateIdentifier
    private String id;

    @AggregateMember
    private String name;

    @AggregateMember
    private int weight;

    @AggregateMember
    private String status;

    public ElephantAggregate() {
    }

    // Axon Server로 푸시 받은 커맨드 메세지가 들어온다.
    // 새로운 애그리게이트의 엔티티를 처리할 때는 생성자 메서드를 이용한다.
    @CommandHandler
    private ElephantAggregate(CreateElephantCommand cmd) {
        log.info("[@CommandHandler 'CreateElephantCommand'] for Id: {}", cmd.getId());
        CreateElephantEvent event = new CreateElephantEvent(cmd.getId(), cmd.getName(), cmd.getWeight(), cmd.getStatus());
        AggregateLifecycle.apply(event);
    }

    // 엔티티의 일부 필드 값만 변경하는 경우 절대 생성자 메서드로 작성하면 안된다.
    // 그렇게 하면 새로운 엔티티가 생성되어 예기치 못한 동작이 발생한다.
    // 보통 일부 필드 값만 변경하는 경우 `handle`이라는 이름의 메서드로 커맨드를 처리한다.
    @CommandHandler
    private void handle(EnterElephantCommand cmd) {
        log.info("[@CommandHandler 'EnterElephantCommand'] for Id: {}", cmd.getId());
        AggregateLifecycle.apply(new EnteredElephantEvent(cmd.getId(), cmd.getStatus()));
    }

    @CommandHandler
    private void handle(ExitElephantCommand cmd) {
        log.info("[@CommandHandler 'ExitElephantCommand'] for Id: {}", cmd.getId());
        AggregateLifecycle.apply(new ExitedElephantEvent(cmd.getId(), cmd.getStatus()));
    }

    @CommandHandler
    private void handle(BackToReadyCommand cmd) {
        log.info("[@CommandHandler 'BackToReadyCommand'] for Id: {}", cmd.getId());
        AggregateLifecycle.apply(new BackToReadyCompletedEvent(cmd.getId(), cmd.getStatus()));
    }

    @EventSourcingHandler
    private void on(CreateElephantEvent event) {
        log.info("[@EventSourcingHandler 'CreateElephantEvent'] for Id: {}", event.getId());
        this.id = event.getId();
        this.name = event.getName();
        this.weight = event.getWeight();
        this.status = event.getStatus();
    }

    @EventSourcingHandler
    private void on(EnteredElephantEvent event) {
        log.info("[@EventSourcingHandler 'EnteredElephantEvent'] for Id: {}", event.getId());
        log.info("======== [넣기] Event Replay => 코끼리 상태: {}", this.status);
        this.status = event.getStatus();
        log.info("======== [넣기] 최종 코끼리 상태: {}", this.status);
    }

    @EventSourcingHandler
    private void on(ExitedElephantEvent event) {
        log.info("[@EventSourcingHandler 'ExitedElephantEvent'] for Id: {}", event.getId());
        log.info("======== [꺼내기] Event Replay => 코끼리 상태: {}", this.status);
        this.status = event.getStatus();
        log.info("======== [꺼내기] 최종 코끼리 상태: {}", this.status);
    }

    @EventSourcingHandler
    private void on(BackToReadyCompletedEvent event) {
        log.info("[@EventSourcingHandler 'BackToReadyCompletedEvent'] for Id: {}", event.getId());
        this.status = event.getStatus();
    }

}
