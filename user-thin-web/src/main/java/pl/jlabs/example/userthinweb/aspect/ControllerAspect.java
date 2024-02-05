package pl.jlabs.example.userthinweb.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ControllerAspect {

    @Pointcut("@within(org.springframework.stereotype.Controller)")
    public void controllerMethod() {}

    @Around("controllerMethod()")
    public Object measureResponseTime(final ProceedingJoinPoint joinPoint) throws Throwable {
        final long startTime = System.currentTimeMillis();
        final Object ret = joinPoint.proceed();
        final long endTime = System.currentTimeMillis();
        final String methodName = joinPoint.getSignature().getName();
        log.info("Call of {} took {} miliseconds", methodName, (endTime - startTime));
        return ret;
    }
}
