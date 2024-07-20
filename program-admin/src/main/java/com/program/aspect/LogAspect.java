package com.program.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final ObjectMapper objectMapper;

    @Pointcut("execution(public * com.program.controller.*.*(..))")
    public void logPoint() {
    }

    @Around("logPoint()")
    public Object aroundLog(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // request info
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        log.info("[Request] {} url: {} headers: {}, execute method: {}#{}",
                request.getMethod(),
                String.format("%s?%s", request.getServletPath(), Optional.ofNullable(request.getQueryString()).orElse("")),
                getRequestHeaders(request),
                point.getSignature().getDeclaringTypeName(),
                point.getSignature().getName()
        );
        Long start = System.currentTimeMillis();
        // execute target method
        Object result = point.proceed();
        Long end = System.currentTimeMillis();
        // response info
        HttpServletResponse response = attributes.getResponse();
        log.info("[Response] {} {}ms, headers: {}, body: {}",
                Objects.requireNonNull(response).getStatus(),
                end - start,
                getResponseHeaders(response),
                objectMapper.writeValueAsString(result)
        );
        return result;
    }

    private Map<String, String> getRequestHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            headers.put(key, value);
        }
        return headers;
    }

    private Map<String, String> getResponseHeaders(HttpServletResponse response) {
        HashMap<String, String> headers = new HashMap<>();
        for (String headerName : response.getHeaderNames()) {
            headers.put(headerName, response.getHeader(headerName));
        }
        return headers;
    }

}
