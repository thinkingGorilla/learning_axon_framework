package me.thinking_gorilla.learning_axon_framework.service;

import lombok.extern.slf4j.Slf4j;
import me.thinking_gorilla.learning_axon_framework.command.CreateElephantCommand;
import me.thinking_gorilla.learning_axon_framework.command.EnterElephantCommand;
import me.thinking_gorilla.learning_axon_framework.command.ExitElephantCommand;
import me.thinking_gorilla.learning_axon_framework.dto.ElephantDTO;
import me.thinking_gorilla.learning_axon_framework.dto.StatusEnum;
import me.thinking_gorilla.learning_axon_framework.entity.Elephant;
import me.thinking_gorilla.learning_axon_framework.queries.GetElephantQuery;
import me.thinking_gorilla.learning_axon_framework.repository.ElephantRepository;
import me.thinking_gorilla.learning_axon_framework.vo.ResultVO;
import org.apache.commons.lang3.RandomStringUtils;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ElephantService {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final ElephantRepository elephantRepository;

    public ElephantService(CommandGateway commandGateway, QueryGateway queryGateway, ElephantRepository elephantRepository) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
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
        CreateElephantCommand cmd = CreateElephantCommand.builder().id(RandomStringUtils.random(3, false, true)).name(elephant.getName()).weight(elephant.getWeight()).status(StatusEnum.READY.value()).build();

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
            commandGateway.sendAndWait(EnterElephantCommand.builder().id(id).status(StatusEnum.ENTER.value()).build(), 30, TimeUnit.SECONDS);
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
            commandGateway.sendAndWait(ExitElephantCommand.builder().id(id).status(StatusEnum.EXIT.value()).build(), 30, TimeUnit.SECONDS);
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

    public ResultVO<Elephant> getElephant(String id) {
        log.info("[ElephantService] Executing getElephant for Id: {}", id);

        ResultVO<Elephant> retVo = new ResultVO<>();

        // Elephant elephant = queryGateway.query(new GetElephantQuery(id), ResponseTypes.instanceOf(Elephant.class)).join();

        Elephant elephant = queryGateway.scatterGather(
                new GetElephantQuery(id),
                ResponseTypes.instanceOf(Elephant.class),
                30,
                TimeUnit.SECONDS
        ).toList().get(0);

        if (elephant != null) {
            retVo.setReturnCode(true);
            retVo.setReturnMessage("ID: " + id);
            retVo.setResult(elephant);
        } else {
            retVo.setReturnCode(false);
            retVo.setReturnMessage("Can't get elephant for id: " + id);
        }

        return retVo;
    }

    public ResultVO<List<Elephant>> getLists() {
        log.info("[ElephantService] Executing getLists");

        ResultVO<List<Elephant>> retVo = new ResultVO<>();

        // P2P Query
        List<Elephant> elephants = queryGateway.query("list", "", ResponseTypes.multipleInstancesOf(Elephant.class)).join();

        retVo.setReturnCode(true);
        retVo.setReturnMessage("코끼리수: " + elephants.size());
        retVo.setResult(elephants);

        return retVo;
    }
}
