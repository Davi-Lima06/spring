package projetoteste.repository;

import jpa.anotations.Repository;
import jpa.databaseacess.SimpleEntityManager;
import projetoteste.entity.Usuario;

@Repository
public class UsuarioRepository extends SimpleEntityManager<Usuario> {

}
