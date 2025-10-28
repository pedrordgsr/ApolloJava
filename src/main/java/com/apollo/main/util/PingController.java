package com.apollo.main.util;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
@Tag(name = "Ping", description = "Checar se a API está ativa")
public class PingController {

    @GetMapping
    public String ping() {
        return "pong";
    }

}
