package me.thinking_gorilla.learning_axon_framework.service;

import lombok.extern.slf4j.Slf4j;
import me.thinking_gorilla.learning_axon_framework.command.CreateElephantCommand;
import me.thinking_gorilla.learning_axon_framework.dto.ElephantDTO;
import me.thinking_gorilla.learning_axon_framework.vo.ResultVO;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ElephantService {

    public ResultVO<CreateElephantCommand> create(ElephantDTO elephant) {
        log.info("[ElephantService] Executing create: {}", elephant.toString());

        ResultVO<CreateElephantCommand> retVo = new ResultVO<>();
        return retVo;
    }

    public ResultVO<String> enter(String id) {
        log.info("[ElephantService] Executing enter for Id: {}", id);

        ResultVO<String> retVo = new ResultVO<>();
        return retVo;
    }

    public ResultVO<String> exit(String id) {
        log.info("[ElephantService] Executing exit for Id: {}", id);

        ResultVO<String> retVo = new ResultVO<>();
        return retVo;
    }

}
