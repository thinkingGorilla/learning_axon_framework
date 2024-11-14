package me.thinking_gorilla.learning_axon_framework.events;

import lombok.AllArgsConstructor;
import lombok.Data;

// 커맨드는 이벤트를 발생시키는 요청 행동이기 때문에 현재형으로 표현하고,
// 이벤트는 발생한 결과이므로 과거형으로 표현한다.
@Data
@AllArgsConstructor
public class CreateElephantEvent {
    private String id;
    private String name;
    private int weight;
    private String status;
}
