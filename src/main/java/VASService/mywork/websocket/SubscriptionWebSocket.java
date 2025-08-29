package VASService.mywork.websocket;

import VASService.mywork.classes.Subscription;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionWebSocket {

    private final SimpMessagingTemplate messagingTemplate;

    public SubscriptionWebSocket(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastStatus(Subscription subscription) {
        messagingTemplate.convertAndSend("/topic/subscriptions", subscription);
    }
}
