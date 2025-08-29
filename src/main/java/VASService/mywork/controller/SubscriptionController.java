package VASService.mywork.controller;

import VASService.mywork.classes.Subscription;
import VASService.mywork.services.SubscriptionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/subscribe")
    public Subscription subscribe(@RequestParam String userPhoneNumber) {
        return subscriptionService.subscribeUser(userPhoneNumber);
    }

    @PostMapping("/unsubscribe")
    public Subscription unsubscribe(@RequestParam String userPhoneNumber) {
        return subscriptionService.unsubscribeUser(userPhoneNumber);
    }

    @PostMapping("/pending")
    public Subscription markPending(@RequestParam String userPhoneNumber) {
        return subscriptionService.markPending(userPhoneNumber);
    }
}
