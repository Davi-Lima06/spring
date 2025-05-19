package framework.rotas;

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
}
