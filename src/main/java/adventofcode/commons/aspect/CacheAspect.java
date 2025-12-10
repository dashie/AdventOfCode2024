package adventofcode.commons.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
public class CacheAspect {

    // TODO configure initial size based on system proprties
    public static final int INITIAL_SIZE = 1024;

    private final Map<String, Object> cache = new HashMap<>(1024);

    /**
     *
     */
    @Around("@annotation(Cache) && execution(* *(..))")
    public Object cacheMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String key = generateKey(joinPoint);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        Object result = joinPoint.proceed();
        cache.put(key, result);
        return result;
    }

    /**
     *
     */
    private String generateKey(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Cache cacheAnnotation = method.getAnnotation(Cache.class);
        String separator = cacheAnnotation.value();
        String methodName = joinPoint.getSignature().toLongString();
        Object[] args = joinPoint.getArgs();
        return methodName + "(" + String.join(separator, toStringArray(args)) + ")";
    }

    /**
     *
     */
    private String[] toStringArray(Object[] args) {
        String[] result = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            result[i] = (args[i] != null) ? args[i].toString() : "null";
        }
        return result;
    }
}