package projetoteste.mapper;

import framework.anotations.type.Component;
import projetoteste.dto.CadastroDTO;
import projetoteste.entity.Usuario;

@Component
public class UsuarioMapper {

    public Usuario toModel(CadastroDTO cadastroDTO) {
        Usuario usuario = new Usuario();
        usuario.setId(cadastroDTO.getId());
        usuario.setNome(cadastroDTO.getNome());

        return usuario;
    }
}
