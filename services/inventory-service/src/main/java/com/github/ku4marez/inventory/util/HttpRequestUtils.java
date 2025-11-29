package com.github.ku4marez.inventory.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HttpRequestUtils {

    private static HttpServletRequest currentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getRequest();
        }
        return null;
    }

    public static String getPath() {
        HttpServletRequest req = currentRequest();
        return req != null ? req.getRequestURI() : "unknown";
    }

    public static String getMethod() {
        HttpServletRequest req = currentRequest();
        return req != null ? req.getMethod() : "unknown";
    }
}
