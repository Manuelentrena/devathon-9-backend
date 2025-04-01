package com.devathon.griffindor_backend.hooks;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.handler.annotation.MessageMapping;

import java.lang.reflect.Method;

@Aspect
@Component
public class MessageMappingLoggingHook {

    @Pointcut("@annotation(org.springframework.messaging.handler.annotation.MessageMapping)")
    public void messageMappingMethods() {
    }

    @Before("messageMappingMethods() && args(message, headerAccessor,..)")
    public void logMessageDetails(JoinPoint joinPoint, String message, SimpMessageHeaderAccessor headerAccessor) {
        // Get Id session
        String sessionId = headerAccessor.getSessionId();

        // Get Name of function
        String methodName = joinPoint.getSignature().getName();

        // Get method class
        Class<?> targetClass = joinPoint.getTarget().getClass();

        try {
            Method method = targetClass.getMethod(methodName, String.class, SimpMessageHeaderAccessor.class);

            // Get anotation @MessageMapping
            MessageMapping messageMapping = method.getAnnotation(MessageMapping.class);
            String destination = messageMapping.value()[0]; // Accedemos a la ruta de @MessageMapping

            // Show log
            System.out.println("""
                    - ENTER MESSAGE
                    📝 Name Method: %s
                    🚀 Entry Path: %s
                    🙍‍♂️ User ID: %s
                    📩 Message:
                    %s
                    """.formatted(methodName, destination, sessionId, message));

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}