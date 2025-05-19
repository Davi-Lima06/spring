package framework.rotas;

import framework.anotations.method.DeleteMapping;
import framework.anotations.method.GetMapping;
import framework.anotations.method.PostMapping;
import framework.anotations.method.PutMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CreateRoutesUtil {

    public static Map<String, Method> getDeleteMappings(Object controllerIntance) {
        Map<String, Method> routes = new HashMap<>();
        for (Method method : controllerIntance.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(DeleteMapping.class)) {
                String path = method.getAnnotation(DeleteMapping.class).value();
                routes.put(path, method);
            }
        }
        return routes;
    }

    public static Map<String, Method> getRouteMappings(Object controllerInstance) {
        Map<String, Method> routes = new HashMap<>();

        for (Method method : controllerInstance.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(GetMapping.class)) {
                String path = method.getAnnotation(GetMapping.class).value();
                routes.put(path, method);
            }
        }
        return routes;
    }

    public static Map<String, Method> getPostMappings(Object controllerInstance) {
        Map<String, Method> routes = new HashMap<>();

        for (Method method : controllerInstance.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostMapping.class)) {
                String path = method.getAnnotation(PostMapping.class).value();
                routes.put(path, method);
            }
        }
        return routes;
    }

    public static Map<String, Method> getPutMappings(Object controllerInstance) {
        Map<String, Method> routes = new HashMap<>();
        for (Method method : controllerInstance.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PutMapping.class)) {
                String path = method.getAnnotation(PutMapping.class).value();
                routes.put(path, method);
            }
        }
        return routes;
    }
}
