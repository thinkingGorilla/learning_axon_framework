package me.thinking_gorilla.learning_axon_framework.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.thinking_gorilla.learning_axon_framework.command.CreateEnterCountCommand;
import me.thinking_gorilla.learning_axon_framework.command.UpdateEnterCountCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.spring.stereotype.Aggregate;

import java.io.Serial;
import java.io.Serializable;

@Aggregate
@Data
@Entity
@Slf4j
@Table(name = "enter_count")
public final class EnterCount implements Serializable {

    @Serial
    private static final long serialVersionUID = 2274667604365914508L;

    @AggregateIdentifier
    @Id
    @Column(name = "count_id", nullable = false, length = 10)
    private String countId;

    @AggregateMember
    @Column(name = "elephant_id", nullable = false, length = 3)
    private String elephantId;

    @AggregateMember
    @Column(name = "count", nullable = false)
    private int count;

    public EnterCount() {
    }

    @CommandHandler
    private EnterCount(CreateEnterCountCommand cmd) {
        log.info("[@CommandHandler] CreateEnterCountCommand for Id: {}", cmd.getElephantId());

        this.countId = cmd.getCountId();
        this.elephantId = cmd.getElephantId();
        this.count = cmd.getCount();
    }

    @CommandHandler
    private void handle(UpdateEnterCountCommand cmd) {
        log.info("[@CommandHandler] UpdateEnterCountCommand for Id: {}", cmd.getElephantId());
        log.info("current count: {} + {}", this.count, cmd.getCount());
        this.count += cmd.getCount();
    }
}
