package com.program.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.program.exception.BusinessException;
import com.program.exception.CommonErrorCode;
import com.program.utils.SignUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.TreeMap;

import static com.program.utils.AnnotationUtils.LogAndInvokeTargetMethod;

@Aspect
@Component
@RequiredArgsConstructor
public class ApiSignAspect {

    ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @Around("execution(public * com.program.controller.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        // 获取目标方法的对象
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> clazz = joinPoint.getTarget().getClass();
        Logger logger = LoggerFactory.getLogger(clazz);

        String requestURL = request.getRequestURL().toString();
        if (requestURL.contains("util") || requestURL.contains("common")) {
            return LogAndInvokeTargetMethod(joinPoint, logger, String.format("%s中的%s方法", clazz.getName(), method.getName()), String.format("%s中的%s方法执行结束", clazz.getName(), method.getName()));
        }

        String sign = Optional.ofNullable(request.getHeader("sign")).orElse("");
        String timestamp = Optional.ofNullable(request.getHeader("x-timestamp")).orElse("");
        String nonce = Optional.ofNullable(request.getHeader("x-nonce")).orElse("");
        String queryString = Optional.ofNullable(request.getQueryString()).orElse("");
        String bodyString = Optional.ofNullable(request.getHeader("body-string")).orElse("");
        TreeMap<String, Object> sortedParams = new TreeMap<>();
        sortedParams.put("x-timestamp", timestamp);
        sortedParams.put("x-nonce", nonce);
        sortedParams.put("query-string", queryString);
        sortedParams.put("body-string", bodyString);
        String originalData = objectMapper.writeValueAsString(sortedParams);
        if (!SignUtils.verify(originalData, SignUtils.getPublicKey(SignUtils.PUBLIC_KEY), sign)) {
            logger.error("sign failed: \n" +
                            "front sign: {}\n" +
                            "backend sign: {}",
                    sign, SignUtils.sign(originalData, SignUtils.getPrivateKey(SignUtils.PRIVATE_KEY))
            );
            throw new BusinessException(CommonErrorCode.SIGNATURE_FAILED);
        }
        return LogAndInvokeTargetMethod(joinPoint, logger, String.format("%s中的%s方法", clazz.getName(), method.getName()), String.format("%s中的%s方法执行结束", clazz.getName(), method.getName()));
    }
}
