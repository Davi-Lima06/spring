package framework.container;

import com.sun.net.httpserver.HttpServer;
import framework.anotations.type.Controller;
import framework.rotas.DeleteHandler;
import framework.rotas.GetHandler;
import framework.rotas.PostHandler;
import framework.rotas.PutHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Map;

public class MiniHttpServer {

    private final ApplicationContext context;

    public MiniHttpServer(ApplicationContext context) {
        this.context = context;
    }

    public void start(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        for (Object bean : context.getBeans()) {
            Class<?> clazz = bean.getClass();
            if (clazz.isAnnotationPresent(Controller.class)) {
                Map<String, Method> gets = context.getRouteMappings(bean);
                Map<String, Method> posts = context.getPostMappings(bean);
                Map<String, Method> puts = context.getPutMappings(bean);
                Map<String, Method> deletes = context.getDeleteMappings(bean);

                for (Map.Entry<String, Method> entry : gets.entrySet()) {
                    String path = entry.getKey();
                    Method method = entry.getValue();
                    server.createContext(path, new GetHandler(method, bean));
                    System.out.println("Route mapped: [GET] " + path + " -> " + method.getName());
                }

                for (Map.Entry<String, Method> entry : posts.entrySet()) {
                    String path = entry.getKey();
                    Method method = entry.getValue();
                    server.createContext(path, new PostHandler(method, bean));
                    System.out.println("Route mapped: [POST] " + path + " -> " + method.getName());
                }

                for (Map.Entry<String, Method> entry : puts.entrySet()) {
                    String path = entry.getKey();
                    Method method = entry.getValue();
                    server.createContext(path, new PutHandler(method, bean));
                    System.out.println("Route mapped: [PUT] " + path + " -> " + method.getName());
                }

                for (Map.Entry<String, Method> entry : deletes.entrySet()) {
                    String path = entry.getKey();
                    Method method = entry.getValue();
                    server.createContext(path, new DeleteHandler(method, bean));
                    System.out.println("Route mapped: [DELETE] " + path + " -> " + method.getName());
                }
            }
        }

        server.setExecutor(null);
        server.start();
        System.out.println("Server started on http://localhost:" + port);
    }
}
