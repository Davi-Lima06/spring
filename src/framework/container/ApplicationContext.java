package framework.container;

import framework.anotations.field.Autowired;
import framework.anotations.method.DeleteMapping;
import framework.anotations.method.GetMapping;
import framework.anotations.method.PostMapping;
import framework.anotations.method.PutMapping;
import framework.anotations.type.Component;
import framework.anotations.type.Controller;
import framework.anotations.type.Service;
import jpa.anotations.Repository;
import jpa.databasecreators.DataBase;
import jpa.databasecreators.TableCreator;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.util.*;

public class ApplicationContext {
    private final Map<Class<?>, Object> beans = new HashMap<>();
    public Map<String, Method> getDeleteMappings;

    public ApplicationContext(String basePackage) {
        try {
            List<Class<?>> classes = scanPackage(basePackage);
            createTables(classes);
            instatiateComponents(classes);
            injectDependencies();
            System.out.println("TUDO CRIADO");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Class<?>> scanPackage(String basePackage) throws ClassNotFoundException {
        String path = basePackage.replace('.', '/');
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        if (resource == null) {
            throw new ClassNotFoundException("Pacote não encontrado: " + basePackage);
        }
        File dir = new File(resource.getFile());
        List<Class<?>> classes = new ArrayList<>();
        scanDirectory(basePackage, dir, classes);
        return classes;
    }

    private void scanDirectory(String basePackage, File directory, List<Class<?>> classes) throws ClassNotFoundException {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                scanDirectory(basePackage + "." + file.getName(), file, classes);
            } else if (file.getName().endsWith(".class")) {
                String className = basePackage + "." + file.getName().replace(".class", "");
                classes.add(Class.forName(className));
            }
        }
    }


    private void instatiateComponents(List<Class<?>> classes) throws Exception {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Controller.class)
            || clazz.isAnnotationPresent(Repository.class)) {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                beans.put(clazz, instance);
            }
        }
    }

    private void injectDependencies() throws IllegalAccessException {
        for (Object bean : beans.values()) {
            for (Field field : bean.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Object dependency = beans.get(field.getType());
                    field.setAccessible(true);
                    field.set(bean, dependency);
                }
            }
        }
    }

    private void createTables(List<Class<?>> models) {
        Connection connection = DataBase.getConnection();
        for (Class<?> model : models ) {
            TableCreator.createTable(model, connection);
        }
    }

    public <T> T getBean(Class<T> clazz) {
        return clazz.cast(beans.get(clazz));
    }

    public Collection<Object> getBeans() {
        return beans.values();
    }
}
