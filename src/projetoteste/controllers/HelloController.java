package projetoteste.controllers;

import framework.anotations.method.PostMapping;
import framework.anotations.method.PutMapping;
import framework.anotations.type.Controller;
import framework.anotations.method.GetMapping;
import projetoteste.dto.CadastroDTO;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Olá do seu próprio framework!";
    }

    @PostMapping("/enviar")
    public String cadastrar(String nome) {
        return "Seu nome é " + nome.replace("{", "").replace("}", "").replace("\"", "");
    }

    @PutMapping("/put")
    public String att(String nome) {
        return "Seu nome é " + nome.replace("{", "").replace("}", "").replace("\"", "");
    }
}
