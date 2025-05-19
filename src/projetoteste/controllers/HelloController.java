package projetoteste.controllers;

import framework.anotations.field.Autowired;
import framework.anotations.method.DeleteMapping;
import framework.anotations.method.PostMapping;
import framework.anotations.method.PutMapping;
import framework.anotations.parameter.RequestBody;
import framework.anotations.parameter.RequestParam;
import framework.anotations.type.Controller;
import framework.anotations.method.GetMapping;
import projetoteste.dto.CadastroDTO;
import projetoteste.entity.Usuario;
import projetoteste.services.UsuarioService;

import java.util.List;

@Controller
public class HelloController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/list")
    public List<Usuario> findAll() {
        return usuarioService.listAll();
    }

    @PostMapping("/persist")
    public String persist(@RequestBody CadastroDTO dto) {
        return usuarioService.persist(dto);
    }

    @GetMapping("/id")
    public Usuario findById(@RequestParam("id") Long id) {
        return usuarioService.findById(id);
    }

    @PutMapping("/put")
    public String att(String nome) {
        return "Seu nome é " + nome.replace("{", "").replace("}", "").replace("\"", "");
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        return usuarioService.deleteById(id);
    }
}
