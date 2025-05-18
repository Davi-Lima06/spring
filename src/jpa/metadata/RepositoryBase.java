package jpa.metadata;

import java.sql.Connection;
import java.util.List;

public interface RepositoryBase<T> {
    Connection connection = DataBase.getConnection();
    public void persist(T entity);
    public List<T> listAll();
    public T findById(Long id);
}
