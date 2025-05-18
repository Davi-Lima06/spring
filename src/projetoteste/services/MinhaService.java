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
        Usuario usuario = new Usuario(null, nome);
        Long id = 1L;
        for (int i = 0; i < listAll().size(); i++) { //enquanto não faco o @RequestBody e o @GeneratedValue
            String banco = findById(id);
            if (banco != null) {
                id++;
            }
        }
        usuario.setId(id);
        usuarioRepository.persist(usuario);
        return "Deu bom";
    }

    public List<String> listAll() {
        List<String> listNames = new ArrayList<>();
        for (Usuario usuario : usuarioRepository.listAll()) {
            listNames.add(usuario.toStringReturn());
        }
        return listNames;
    }

    public String doSomething(String nome) {
        return "\nTu eres " + nome + "\n";
    }

    public String findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id);
        return usuario.toStringReturn();
    }
}
