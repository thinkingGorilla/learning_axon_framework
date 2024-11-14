package me.thinking_gorilla.learning_axon_framework.command;

import lombok.Builder;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
@Builder
public class CreateElephantCommand {
    // 커맨드가 어그리게이트로 전달 될 때, 어떤 코끼리 객체에 대해 이벤트 리플레이를 할 것인지 알아야 한다.
    // 따라서 각 코끼리 객체를 구별할 수 있는 고유값을 아래의 어노테이션으로 지정한다.
    @TargetAggregateIdentifier
    String id;

    String name;
    int weight;
    String status;
}
