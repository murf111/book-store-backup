package com.epam.rd.autocode.spring.project.aspect;

import com.epam.rd.autocode.spring.project.annotation.Sensitive;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    // 1. POINTCUT: Selects all methods in your Service layer
    @Pointcut("execution(* com.epam.rd.autocode.spring.project.service..*(..))")
    public void serviceMethods() {}

    // 2. ENTRY LOGGING (Handles Security Masking)
    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.toShortString();

        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = signature.getMethod().getParameters();

        List<String> safeArgs = new ArrayList<>();

        // Loop through args to check for @Sensitive
        for (int i = 0; i < args.length; i++) {
            if (parameters[i].isAnnotationPresent(Sensitive.class)) {
                safeArgs.add(parameters[i].getName() + "=[***PROTECTED***]");
            } else {
                safeArgs.add(String.valueOf(args[i]));
            }
        }

        log.info(">> BUSINESS EVENT: Starting {} | Args: {}", methodName, safeArgs);
    }

    // 3. EXIT LOGGING (Handles Large Lists/Pages)
    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().toShortString();
        String resultSummary;

        if (result == null) {
            resultSummary = "null";
        } else if (result instanceof Collection<?>) {
            resultSummary = "Collection(size=" + ((Collection<?>) result).size() + ")";
        } else if (result instanceof Page<?>) {
            resultSummary = "Page(totalElements=" + ((Page<?>) result).getTotalElements() + ")";
        } else {
            resultSummary = result.toString();
        }

        log.info("<< BUSINESS SUCCESS: Finished {} | Returned: {}", methodName, resultSummary);
    }

    // 4. EXCEPTION LOGGING (Handles Errors)
    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().toShortString();

        // Log the error message and the stack trace
        log.error("!! BUSINESS ERROR: Exception in {} | Message: {}", methodName, ex.getMessage(), ex);
    }
}