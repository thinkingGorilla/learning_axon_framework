package me.thinking_gorilla.learning_axon_framework.service;

import lombok.extern.slf4j.Slf4j;
import me.thinking_gorilla.learning_axon_framework.command.CreateElephantCommand;
import me.thinking_gorilla.learning_axon_framework.command.EnterElephantCommand;
import me.thinking_gorilla.learning_axon_framework.command.ExitElephantCommand;
import me.thinking_gorilla.learning_axon_framework.dto.ElephantDTO;
import me.thinking_gorilla.learning_axon_framework.dto.StatusEnum;
import me.thinking_gorilla.learning_axon_framework.entity.Elephant;
import me.thinking_gorilla.learning_axon_framework.repository.ElephantRepository;
import me.thinking_gorilla.learning_axon_framework.vo.ResultVO;
import org.apache.commons.lang3.RandomStringUtils;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ElephantService {

    // Axon Framework에서 메세지를 송수신하는 class들은 반드시 'transient' 키워드를 붙여야 한다.
    private final transient CommandGateway commandGateway;
    private final ElephantRepository elephantRepository;

    public ElephantService(CommandGateway commandGateway, ElephantRepository elephantRepository) {
        this.commandGateway = commandGateway;
        this.elephantRepository = elephantRepository;
    }

    public ResultVO<CreateElephantCommand> create(ElephantDTO elephant) {
        log.info("[ElephantService] Executing create: {}", elephant.toString());

        ResultVO<CreateElephantCommand> retVo = new ResultVO<>();

        if (elephant.getWeight() < 30 || elephant.getWeight() > 200) {
            retVo.setReturnCode(false);
            retVo.setReturnMessage("몸무게는 30kg 이상 200kg 이하로 입력해 주세요.");
            return retVo;
        }

        // Axon Server로 이벤트를 송신한다.
        CreateElephantCommand cmd = CreateElephantCommand.builder()
                .id(RandomStringUtils.random(3, false, true))
                .name(elephant.getName())
                .weight(elephant.getWeight())
                .status(StatusEnum.READY.value())
                .build();

        try {
            commandGateway.sendAndWait(cmd, 30, TimeUnit.SECONDS);
            retVo.setReturnCode(true);
            retVo.setReturnMessage("Success to create elephant");
            retVo.setResult(cmd);
        } catch (Exception e) {
            retVo.setReturnCode(false);
            retVo.setReturnMessage(e.getMessage());
        }

        return retVo;
    }

    public ResultVO<String> enter(String id) {
        log.info("[ElephantService] Executing enter for Id: {}", id);

        ResultVO<String> retVo = new ResultVO<>();

        Elephant elephant = getEntity(id);
        if (elephant.getStatus().equals(StatusEnum.ENTER.value())) {
            retVo.setReturnCode(false);
            retVo.setReturnMessage("이미 냉장고 안에 있는 코끼리입니다.");
            return retVo;
        }

        try {
            commandGateway.sendAndWait(
                    EnterElephantCommand.builder()
                            .id(id)
                            .status(StatusEnum.ENTER.value())
                            .build(),
                    30,
                    TimeUnit.SECONDS
            );
            retVo.setReturnCode(true);
            retVo.setReturnMessage("Success to request enter elephant");
        } catch (Exception e) {
            retVo.setReturnCode(false);
            retVo.setReturnMessage(e.getMessage());
        }

        return retVo;
    }

    public ResultVO<String> exit(String id) {
        log.info("[ElephantService] Executing exit for Id: {}", id);

        ResultVO<String> retVo = new ResultVO<>();

        Elephant elephant = getEntity(id);
        if (!elephant.getStatus().equals(StatusEnum.ENTER.value())) {
            retVo.setReturnCode(false);
            retVo.setReturnMessage("냉장고 안에 있는 코끼리만 꺼낼 수 있습니다.");
            return retVo;
        }

        try {
            commandGateway.sendAndWait(
                    ExitElephantCommand.builder()
                            .id(id)
                            .status(StatusEnum.EXIT.value())
                            .build(),
                    30,
                    TimeUnit.SECONDS
            );
            retVo.setReturnCode(true);
            retVo.setReturnMessage("Success to request exit elephant");
        } catch (Exception e) {
            retVo.setReturnCode(false);
            retVo.setReturnMessage(e.getMessage());
        }

        return retVo;
    }


    private Elephant getEntity(String id) {
        Optional<Elephant> optElephant = elephantRepository.findById(id);
        return optElephant.orElse(null);
    }
}
