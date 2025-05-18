package projetoteste.controllers;

import framework.anotations.field.Autowired;
import framework.anotations.method.DeleteMapping;
import framework.anotations.method.PostMapping;
import framework.anotations.method.PutMapping;
import framework.anotations.parameter.RequestParam;
import framework.anotations.type.Controller;
import framework.anotations.method.GetMapping;
import projetoteste.dto.CadastroDTO;
import projetoteste.entity.Usuario;
import projetoteste.services.MinhaService;

import java.util.List;

@Controller
public class HelloController {

    @Autowired
    MinhaService minhaService;

    @GetMapping("/list")
    public List<String> findAll() {
        return minhaService.listAll();
    }

    @PostMapping("/persist")
    public String persist(String nome) {
        return minhaService.persist(nome);
    }

    @GetMapping("/id")
    public String findById(@RequestParam("id") Long id) {
        return minhaService.findById(id);
    }

    @PutMapping("/put")
    public String att(String nome) {
        return "Seu nome é " + nome.replace("{", "").replace("}", "").replace("\"", "");
    }

    @DeleteMapping("/delete")
    public String delete(String nome) {
        return minhaService.doSomething(nome);
    }
}
