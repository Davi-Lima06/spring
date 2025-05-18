package framework.rotas;

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
        Map<String, String> queryParams = parseQueryParams(query);
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (parameter.isAnnotationPresent(RequestParam.class)) {
                RequestParam rp = parameter.getAnnotation(RequestParam.class);
                String value = queryParams.get(rp.value());
                if (parameter.getType() == int.class || parameter.getType() == Integer.class) {
                    args[i] = Integer.parseInt(value);
                } else if (parameter.getType() == boolean.class || parameter.getType() == Boolean.class) {
                    args[i] = Boolean.parseBoolean(value);
                } else if (parameter.getType() == long.class || parameter.getType() == Long.class) {
                    args[i] = Long.parseLong(value);
                } else {
                    args[i] = value;
                }
            } else {
                args[i] = null;
            }
        }

        Object result = null;
        try {
            result = method.invoke(bean, args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        byte[] bytes = result.toString().getBytes();
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
///cadastrar?nome=joao&idade=20&ativo=true
    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    params.put(URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8),
                            URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8));
                }
            }
        }
        return params;
    }

}
