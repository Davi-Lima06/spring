package projetoteste.services;

import framework.anotations.field.Autowired;
import framework.anotations.type.Service;
import projetoteste.dto.CadastroDTO;
import projetoteste.entity.Usuario;
import projetoteste.mapper.UsuarioMapper;
import projetoteste.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    UsuarioMapper usuarioMapper;

    public String persist(CadastroDTO dto) {

        try {
            Usuario usuarioBanco = findById(dto.getId()); //tá dessa forma pq por algum motivo quando
            if (usuarioBanco != null) {                  //não tem dados no banco gera uma exception
                return "ESSE ID JÁ EXISTE";
            }
            Usuario usuario = usuarioMapper.toModel(dto);
            usuarioRepository.persist(usuario);
            return "Deu bom";

        } catch (Exception e) {
            Usuario usuario = usuarioMapper.toModel(dto);
            usuarioRepository.persist(usuario);
            return "Deu bom";
        }
    }

    public String deleteById(Long id) {
        usuarioRepository.deleteById(id);

        return "Excluiu";
    }

    public List<Usuario> listAll() {
        return new ArrayList<>(usuarioRepository.listAll());
    }

    public String doSomething(String nome) {
        return "\nTu eres " + nome + "\n";
    }

    public Usuario findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id);
        return usuario;
    }
}
