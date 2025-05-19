package jpa.databasecreators;

import jpa.anotations.Column;
import jpa.anotations.Entity;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TableCreator {
    public static void createTable(Class<?> clazz, Connection conn) {
        if (!clazz.isAnnotationPresent(Entity.class)) return;
        Entity entity = clazz.getAnnotation(Entity.class);
        String tableName = entity.name().isEmpty() ? clazz.getSimpleName().toLowerCase() : entity.name();
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");
        Field[] fields = clazz.getDeclaredFields();
        List<String> columns = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                String colName = column.name().isEmpty() ? field.getName() : column.name();
                columns.add(colName + " " + column.type());
            }
        }
        sb.append(String.join(", ", columns));
        sb.append(");");
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sb.toString());
            System.out.println("Tabela criada: " + tableName);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela " + tableName, e);
        }
    }
}