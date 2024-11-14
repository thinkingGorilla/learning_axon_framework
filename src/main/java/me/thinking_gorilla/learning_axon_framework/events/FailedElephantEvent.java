package me.thinking_gorilla.learning_axon_framework.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FailedElephantEvent {
    private String id;
}
