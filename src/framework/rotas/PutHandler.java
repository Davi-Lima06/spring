package framework.rotas;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PutHandler implements HttpHandler {

    public final Method method;
    public final Object bean;

    public PutHandler(Method method, Object bean) {
        this.method = method;
        this.bean = bean;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"PUT".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
        }

        String requestBody = new String(exchange.getRequestBody().readAllBytes());

        Object result = null;
        try {
            if (method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(String.class)) {
                result = method.invoke(bean, requestBody);
            } else {
                result = method.invoke(bean);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            exchange.sendResponseHeaders(500, -1);
            return;
        }

        byte[] response = result.toString().getBytes();
        exchange.sendResponseHeaders(200, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }
}
