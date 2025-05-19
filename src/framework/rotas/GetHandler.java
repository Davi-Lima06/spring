package framework.rotas;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import framework.anotations.parameter.RequestParam;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class GetHandler implements HttpHandler {
    private final Gson gson = new Gson();
    private final Method method;
    private final Object bean;

    public GetHandler(Method method, Object bean) {
        this.method = method;
        this.bean = bean;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }
        String query = exchange.getRequestURI().getQuery();
        Object[] args = Util.getValuesArgs(query, method);

        Object result = null;
        try {
            result = method.invoke(bean, args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        String json = gson.toJson(result);
        byte[] bytes = json.getBytes();

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
