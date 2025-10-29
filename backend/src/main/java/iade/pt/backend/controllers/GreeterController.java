package iade.pt.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/api/greeter")
public class GreeterController {
    private Logger logger = LoggerFactory.getLogger(GreeterController.class);
    
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getGreeting() {
        logger.info("Dizendo Olá para o mundo");
        return "Olá Mundo";
    }
    
    @GetMapping(path = "{nome}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getGreetingWithName(@PathVariable("nome") String nome) {
        logger.info("Dizendo Olá para " + nome);
        return "Olá " + nome;
    }
}