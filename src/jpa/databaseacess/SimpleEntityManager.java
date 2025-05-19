package jpa.databaseacess;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleEntityManager<T> implements RepositoryBase<T> {

    private final Class<T> entity = getEntityType();
    EntityMetadata meta = new EntityMetadata(entity);

    public void persist(T entityModel) {
        try {
            String sql = meta.getInsertSQL();
            PreparedStatement stmt = connection.prepareStatement(sql);
            int index = 1;
            for (Field field : meta.getColumns().values()) {
                stmt.setObject(index++, field.get(entityModel));
            }
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteById(Long id) {
        try {
            String sql = meta.getDeleteSql(id);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<T> listAll() {
        List<T> results = new ArrayList<>();
        try {
            String sql = meta.getListAllSQL();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                T instance = entity.getDeclaredConstructor().newInstance();
                for (Map.Entry<String, Field> entry : meta.getColumns().entrySet()) {
                    String columnName = entry.getKey();
                    Field field = entry.getValue();
                    field.setAccessible(true);
                    Object value = columnName.equals(meta.getIdField().getName()) ? Long.valueOf((String) resultSet.getObject(columnName))
                            : resultSet.getObject(columnName);

                    field.set(instance, value);
                }
                results.add(instance);
            }
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    public T findById(Long id) {
        List<T> results = new ArrayList<>();
        try {
            String sql = meta.getFindByIdSQL(id);
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                T instance = entity.getDeclaredConstructor().newInstance();
                for (Map.Entry<String, Field> entry : meta.getColumns().entrySet()) {
                    String columnName = entry.getKey();
                    Field field = entry.getValue();
                    field.setAccessible(true);
                    Object value = columnName.equals(meta.getIdField().getName()) ? Long.valueOf((String) resultSet.getObject(columnName))
                            : resultSet.getObject(columnName);
                    field.set(instance, value);
                }
                results.add(instance);
            }
            return !results.isEmpty() ? results.get(0) : null;
        } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Class<T> getEntityType() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) type.getActualTypeArguments()[0];
    }
}
