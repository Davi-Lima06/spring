package projetoteste.repository;

import jpa.anotations.Repository;
import jpa.metadata.SimpleEntityManager;
import projetoteste.entity.Usuario;

@Repository
public class UsuarioRepository extends SimpleEntityManager<Usuario> {

}
