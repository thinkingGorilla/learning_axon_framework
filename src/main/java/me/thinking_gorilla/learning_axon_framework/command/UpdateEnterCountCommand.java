package me.thinking_gorilla.learning_axon_framework.command;

import lombok.Builder;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
@Builder
public class UpdateEnterCountCommand {
    @TargetAggregateIdentifier
    String countId;
    String elephantId;
    int count;
}
