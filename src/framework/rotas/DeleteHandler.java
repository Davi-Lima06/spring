package framework.rotas;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import framework.anotations.parameter.RequestParam;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

import static framework.rotas.Util.parseQueryParams;

public class DeleteHandler implements HttpHandler {

    private final Method method;
    private final Object bean;

    public DeleteHandler(Method method, Object bean) {
        this.method = method;
        this.bean = bean;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"DELETE".equals(exchange.getRequestMethod())) {
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
                args[i] = Util.validateTypeValue(value, parameter);
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
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        byte[] response = result.toString().getBytes();
        exchange.sendResponseHeaders(200, response.length);
        try(OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }
}
