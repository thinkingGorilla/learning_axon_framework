package me.thinking_gorilla.learning_axon_framework.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultVO<T> {
    private boolean returnCode;
    private String returnMessage;
    private T result;
}
