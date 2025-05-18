package projetoteste.services;

import framework.anotations.field.Autowired;
import framework.anotations.type.Service;
import projetoteste.entity.Usuario;
import projetoteste.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MinhaService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public String persist(String nome) {
        Usuario usuario = new Usuario(1L, nome);
        usuarioRepository.persist(usuario);
        return "Deu bom";
    }

    public List<String> listAll() {

        List<String> listNames = new ArrayList<>();

        for (Usuario usuario : usuarioRepository.listAll()) {
            listNames.add(usuario.getNome());
        }

        return listNames;
    }

    public String doSomething(String nome) {
        return "\nTu eres " + nome + "\n";
    }
}
