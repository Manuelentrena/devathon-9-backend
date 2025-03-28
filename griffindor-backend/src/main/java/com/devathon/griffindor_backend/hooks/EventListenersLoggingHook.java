package com.devathon.griffindor_backend.hooks;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.devathon.griffindor_backend.events.PlayerCountUpdateEvent;

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
        // Get name of method
        String methodName = joinPoint.getSignature().getName();

        // Show details of event
        System.out.println(" ");
        System.out.println("- EVENT LISTENER: ");
        System.out.println("📝 Name Method: " + methodName);
        System.out.println("📢 Event Executed: " + event.getClass().getSimpleName());

        if (event instanceof PlayerCountUpdateEvent) {
            PlayerCountUpdateEvent playerCountEvent = (PlayerCountUpdateEvent) event;
            System.out.println("🚀 Output Path: " + playerCountEvent.getDestination());
            System.out.println("📩 Message:\n" + playerCountEvent.toJson());
        }

        if (event instanceof SessionConnectedEvent) {
            SessionConnectedEvent connectedEvent = (SessionConnectedEvent) event;
            String sessionId = (String) connectedEvent.getMessage().getHeaders().get("simpSessionId");
            System.out.println("🙎‍♂️ User ID: " + sessionId);
            System.out.println("📩 Message: 🟢 A client has connected.");
        }

        if (event instanceof SessionDisconnectEvent) {
            SessionDisconnectEvent disConnectedEvent = (SessionDisconnectEvent) event;
            String sessionId = (String) disConnectedEvent.getMessage().getHeaders().get("simpSessionId");
            System.out.println("🙎‍♂️ User ID: " + sessionId);
            System.out.println("📩 Message: 🔴 A client has disconnected.");
        }

        System.out.println(" ");
    }
}
