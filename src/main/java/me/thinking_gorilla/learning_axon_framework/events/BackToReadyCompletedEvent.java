package me.thinking_gorilla.learning_axon_framework.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BackToReadyCompletedEvent {
    private String id;
    private String status;
}
