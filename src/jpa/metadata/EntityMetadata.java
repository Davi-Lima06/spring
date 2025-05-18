package jpa.metadata;

import jpa.anotations.Column;
import jpa.anotations.Entity;
import jpa.anotations.Id;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class EntityMetadata {

    private Class<?> clazz;
    private String tableName;
    private Field idField;
    private Map<String, Field> columns = new HashMap<>();

    public EntityMetadata(Class<?> clazz) {
        this.clazz = clazz;
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException("Classe não é uma entidade: " + clazz.getName());
        }
        Entity entity = clazz.getAnnotation(Entity.class);
        this.tableName = entity.name().isEmpty() ? clazz.getSimpleName().toLowerCase() : entity.name();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                Column col = field.getAnnotation(Column.class);
                columns.put(col.name().isEmpty() ? field.getName() : col.name(), field);
                this.idField = field;
                field.setAccessible(true);
            } else if (field.isAnnotationPresent(Column.class)) {
                Column col = field.getAnnotation(Column.class);
                columns.put(col.name().isEmpty() ? field.getName() : col.name(), field);
                field.setAccessible(true);
            }
        }
        if (idField == null) {
            throw new RuntimeException("Entidade sem @Id: " + clazz.getName());
        }
    }

    public String getInsertSQL() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(tableName).append(" (");
        StringJoiner columnsJoiner = new StringJoiner(", ");
        StringJoiner valuesJoiner = new StringJoiner(", ");
        for (String columnName : columns.keySet()) {
            columnsJoiner.add(columnName);
            valuesJoiner.add("?");
        }
        sql.append(columnsJoiner.toString()).append(") VALUES (").append(valuesJoiner.toString()).append(")");
        return sql.toString();
    }

    public String getListAllSQL() {
        return "SELECT * FROM " + tableName;
    }

    public String getFindByIdSQL(Long id) throws IllegalAccessException {
        return "SELECT * FROM " + tableName + " WHERE ID = " + id;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getTableName() {
        return tableName;
    }

    public Field getIdField() {
        return idField;
    }

    public Map<String, Field> getColumns() {
        return columns;
    }
}
