package me.thinking_gorilla.learning_axon_framework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.thinking_gorilla.learning_axon_framework.command.CreateElephantCommand;
import me.thinking_gorilla.learning_axon_framework.dto.ElephantDTO;
import me.thinking_gorilla.learning_axon_framework.service.ElephantService;
import me.thinking_gorilla.learning_axon_framework.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Order service API", description = "Order service API")
@Slf4j
@RestController
@RequestMapping("/api/v1")
public class APIController {
    private final ElephantService elephantService;

    @Autowired
    public APIController(ElephantService elephantService) {
        this.elephantService = elephantService;
    }

    @PostMapping("/create")
    @Operation(summary = "코끼리 생성 API")
    private ResultVO<CreateElephantCommand> create(@RequestBody ElephantDTO elephant) {
        log.info("[@PostMapping '/create'] Executing create: {}", elephant.toString());
        return elephantService.create(elephant);
    }

    @PostMapping("/enter/{id}")
    @Operation(summary = "냉장고에 넣기 API")
    @Parameters({
            @Parameter(name = "id", in = ParameterIn.PATH, description = "코끼리 ID", required = true)
    })
    private ResultVO<String> enter(@PathVariable(name = "id") String id) {
        log.info("[@PostMapping '/enter'] Id: {}", id);
        return elephantService.enter(id);
    }

    @PostMapping("/exit/{id}")
    @Operation(summary = "냉장고에서 꺼내기 API")
    @Parameters({
            @Parameter(name = "id", in = ParameterIn.PATH, description = "코끼리 ID", required = true)
    })
    private ResultVO<String> exit(@PathVariable(name = "id") String id) {
        log.info("[@PostMapping '/exit'] Id: {}", id);
        return elephantService.exit(id);
    }
}
