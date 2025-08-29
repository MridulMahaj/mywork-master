package VASService.mywork.controller;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/status")
    @SendTo("/topic/subscription")
    public String broadcastStatus(String message) {
        return message;
    }
}