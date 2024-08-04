package com.program.aspect;

import com.program.annotation.Cache;
import com.program.exception.BusinessException;
import com.program.exception.CommonErrorCode;
import com.program.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.program.utils.AnnotationUtils.LogAndInvokeTargetMethod;
import static com.program.utils.AnnotationUtils.parseSpel;

@Aspect
@Component
@RequiredArgsConstructor
public class CacheAspect {


    private final RedisUtil redisUtil;

    @Around("@annotation(com.program.annotation.Cache)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取目标方法的对象
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 目标方法上的注解对象
        Cache cache = method.getAnnotation(Cache.class);
        String cacheKey = cache.prefix();
        if (StringUtils.hasLength(cache.suffix())) {
            // 解析spel
            String suffix = parseSpel(cache.suffix(), method, joinPoint.getArgs());
            cacheKey += ":" + suffix;
        }

        TimeUnit timeUnit = cache.timeUnit();
        long ttl = cache.ttl();
        int randomTime = cache.randomTime();
        long randomTtl = timeUnit.convert(new Random().nextInt(randomTime), timeUnit);

        // 获取执行这个方法的类
        Class clazz = joinPoint.getTarget().getClass();
        Logger logger = LoggerFactory.getLogger(clazz);

        // 如果设置resetCache为true，则先执行目标方法，再设置缓存
        if (cache.resetCache()) {
            // 记录日志并执行目标方法，返回result为目标方法的返回值
            Object result = LogAndInvokeTargetMethod(joinPoint, logger,
                    String.format("%s中的%s方法, 准备reset cache: %s", clazz.getName(), method.getName(), cacheKey)
                    , String.format("%s中的%s方法执行结束", clazz.getName(), method.getName()));
            if (result == null) {
                throw new BusinessException(CommonErrorCode.E_800001);
            }
            redisUtil.set(cacheKey, result, ttl + randomTtl, timeUnit);
            return result;
        }

        // 如果设置removeCache为true，则删除缓存, 并执行目标方法
        if(cache.removeCache()){
            // 以通配符，删除缓存
            redisUtil.deleteMatchingKeys(cacheKey + ":*");
            return  LogAndInvokeTargetMethod(joinPoint, logger,
                    String.format("%s中的%s方法, 查询了cache: %s", clazz.getName(), method.getName(), cacheKey)
                    , String.format("%s中的%s方法执行结束", clazz.getName(), method.getName()));
        }

        return beforeInvokeCheckCache(joinPoint, method, cacheKey, timeUnit, ttl, randomTtl, clazz, logger);
    }

    private Object beforeInvokeCheckCache(ProceedingJoinPoint joinPoint, Method method, String cacheKey, TimeUnit timeUnit, long ttl, long randomTtl, Class clazz, Logger logger) throws Throwable {
        Object cacheValue = redisUtil.get(cacheKey);
        if (cacheValue == null) {
            Object result = LogAndInvokeTargetMethod(joinPoint, logger,
                    String.format("%s中的%s方法, 查询了cache: %s", clazz.getName(), method.getName(), cacheKey)
                    , String.format("%s中的%s方法执行结束", clazz.getName(), method.getName()));
            redisUtil.set(cacheKey, result, ttl + randomTtl, timeUnit);
            return result;
        }
        return cacheValue;
    }
}
