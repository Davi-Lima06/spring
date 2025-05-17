package projetoteste.repository;

import jpa.metadata.SimpleEntityManager;

import java.sql.Connection;

public class UsuarioRepository extends SimpleEntityManager {
    public UsuarioRepository(Connection connection) {
        super(connection);
    }
}
