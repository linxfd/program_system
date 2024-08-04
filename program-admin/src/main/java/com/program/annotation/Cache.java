package com.program.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {

    // 缓存前缀
    String prefix() default "";

    // spel表达式
    String suffix() default "";

    // 缓存过期时间数量
    long ttl() default 300;

    // 随机数范围，每个ttl后加随机的过期时间, 防止缓存雪崩
    int randomTime() default 5;

    // 缓存过期时间单位
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 如果为true则执行目标方法后, 重设cache
     * 这个目标方法必须返回**需要缓存的数据**
     */
    boolean resetCache() default false;

    /**
     * 如果为true则删除cache
     * 删除的key为prefix + :* ,通配符删除所有
     * @return
     */
    boolean removeCache() default false;
}
