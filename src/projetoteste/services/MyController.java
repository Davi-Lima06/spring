package projetoteste.services;

import framework.anotations.field.Autowired;
import framework.anotations.type.Component;

@Component
public class MyController {

    @Autowired
    MinhaService service;

    public void handleRequest() {
        service.doSomething();
    }
}
