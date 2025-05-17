package jpa.metadata;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleEntityManager {

    private Connection connection;

    public SimpleEntityManager(Connection connection) {
        this.connection = connection;
    }

    public <T> void persist(T entity) {
        try {
            EntityMetadata meta = new EntityMetadata(entity.getClass());
            String sql = meta.getInsertSQL();
            PreparedStatement stmt = connection.prepareStatement(sql);

            int index = 1;
            for (Field field : meta.getColumns().values()) {
                stmt.setObject(index++, field.get(entity));
            }

            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> listAll(Class<T> entity) {

        List<T> results = new ArrayList<>();
        try {
            EntityMetadata meta = new EntityMetadata(entity);
            String sql = meta.getListAllSQL();
            PreparedStatement stmt = connection.prepareStatement(sql);

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                T instance = entity.getDeclaredConstructor().newInstance();

                for (Map.Entry<String, Field> entry : meta.getColumns().entrySet()) {
                    String columnName = entry.getKey();
                    Field field = entry.getValue();

                    field.setAccessible(true);

                    Object value = resultSet.getObject(columnName);
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
}
