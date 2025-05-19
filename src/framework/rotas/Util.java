package framework.rotas;

import framework.anotations.parameter.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Util {

    public static Map<String, String> parseQueryParams(String query) {
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

    public static Object validateTypeValue(String value, Parameter parameter) {
        if (parameter.getType() == int.class || parameter.getType() == Integer.class) {
            return Integer.parseInt(value);
        } else if (parameter.getType() == boolean.class || parameter.getType() == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (parameter.getType() == long.class || parameter.getType() == Long.class) {
            return Long.parseLong(value);
        } else {
            return value;
        }
    }

    public static Object[] getValuesArgs(String query, Method method) {
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
        return args;
    }
}
