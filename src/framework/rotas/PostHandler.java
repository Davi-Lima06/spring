package framework.rotas;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import framework.anotations.parameter.RequestBody;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class PostHandler implements HttpHandler {
    private final Method method;
    private final Object bean;
    private final Gson gson = new Gson();

    public PostHandler(Method method, Object bean) {
        this.method = method;
        this.bean = bean;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }
        String requestBody = new String(exchange.getRequestBody().readAllBytes());

        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];

        for(int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (parameter.isAnnotationPresent(RequestBody.class)) {
                Class<?> paramType = parameter.getType();
                args[i] = gson.fromJson(requestBody, paramType);
            } else {
                args[i] = null;
            }
        }
        Object result = null;
        try {
            if (method.getParameterCount() == 1) {
                result = method.invoke(bean, args);
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
