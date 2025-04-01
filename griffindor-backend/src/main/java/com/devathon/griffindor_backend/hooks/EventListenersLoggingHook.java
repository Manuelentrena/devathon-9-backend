package com.devathon.griffindor_backend.hooks;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
// import com.devathon.griffindor_backend.events.PlayersConnectedEvent;

@Aspect
@Component
public class EventListenersLoggingHook {

    // Pointcut for methods annotated with @EventListener
    @Pointcut("@annotation(org.springframework.context.event.EventListener)")
    public void eventListenerMethods() {
    }

    // Intercept method execution with @EventListener
    @Before("eventListenerMethods() && args(event,..)")
    public void logEventDetails(JoinPoint joinPoint, Object event) {
        String methodName = joinPoint.getSignature().getName();
        String eventName = event.getClass().getSimpleName();

        StringBuilder log = new StringBuilder();

        if (event instanceof SessionConnectedEvent connectedEvent) {
            String sessionId = (String) connectedEvent.getMessage().getHeaders().get("simpSessionId");
            log.append("\n");
            log.append("- EVENT LISTENER:\n");
            log.append("📝 Name Method: ").append(methodName).append("\n");
            log.append("📢 Event Executed: ").append(eventName).append("\n");
            log.append("🙎‍♂️ User ID: ").append(sessionId).append("\n");
            log.append("📩 Message: 🟢 A client has connected.\n");
        }

        if (event instanceof SessionDisconnectEvent disconnectedEvent) {
            String sessionId = (String) disconnectedEvent.getMessage().getHeaders().get("simpSessionId");
            log.append("\n");
            log.append("- EVENT LISTENER:\n");
            log.append("📝 Name Method: ").append(methodName).append("\n");
            log.append("📢 Event Executed: ").append(eventName).append("\n");
            log.append("🙎‍♂️ User ID: ").append(sessionId).append("\n");
            log.append("📩 Message: 🔴 A client has disconnected.\n");
        }

        log.append("\n");
        System.out.println(log);
    }
}
