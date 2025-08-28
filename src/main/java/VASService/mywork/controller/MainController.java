package VASService.mywork.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController

public class MainController {

    private final RestTemplate restTemplate = new RestTemplate();

    // ================= Existing Services =================

    @GetMapping("/cricket")
    public String getCricketData() {
        String url = "http://localhost:8083/matches/live";
        return restTemplate.getForObject(url, String.class);
    }

    @GetMapping("/movies")
    public String getMovieData() {
        String url = "http://localhost:8084/api/movies";
        return restTemplate.getForObject(url, String.class);
    }

    @GetMapping("/tetris")
    public String getTetrisData() {
        String url = "http://localhost:8085/api/tetris";
        return restTemplate.getForObject(url, String.class);
    }

    @GetMapping("/weather")
    public String getWeatherData(@RequestParam String city) {
        String url = "http://localhost:8082/api/weather/current?city=" + city;
        return restTemplate.getForObject(url, String.class);
    }

    @GetMapping("/news")
    public String getNewsData(@RequestParam String story) {
        String url = "http://localhost:8081/api/news/full-story?story=" + story;
        return restTemplate.getForObject(url, String.class);
    }

    // ================= Subscription Flow (via OTP) =================

    @PostMapping("/subscribe")
    public String subscribe(@RequestParam String phoneNumber) {
        String url = "http://localhost:8080/sendotp?phone=" + phoneNumber;
        String response = restTemplate.getForObject(url, String.class);
        return "OTP sent to " + phoneNumber + ". Response: " + response;
    }

    @PostMapping("/verify-subscribe")
    public String verifySubscribe(@RequestParam String phoneNumber, @RequestParam String otp) {
        String url = "http://localhost:8080/verifyotp?phone=" + phoneNumber + "&otp=" + otp;
        String response = restTemplate.getForObject(url, String.class);

        if ("success".equalsIgnoreCase(response)) {
            // TODO: insert into subscriptions table in MySQL
            return "Subscription successful for " + phoneNumber;
        } else {
            return "OTP verification failed for subscription.";
        }
    }

    @PostMapping("/unsubscribe")
    public String unsubscribe(@RequestParam String phoneNumber) {
        String url = "http://localhost:8080/sendotp?phone=" + phoneNumber;
        String response = restTemplate.getForObject(url, String.class);
        return "OTP sent for unsubscribe to " + phoneNumber + ". Response: " + response;
    }

    @PostMapping("/verify-unsubscribe")
    public String verifyUnsubscribe(@RequestParam String phoneNumber, @RequestParam String otp) {
        String url = "http://localhost:8080/verifyotp?phone=" + phoneNumber + "&otp=" + otp;
        String response = restTemplate.getForObject(url, String.class);

        if ("success".equalsIgnoreCase(response)) {
            // TODO: remove from subscriptions table in MySQL
            return "Unsubscription successful for " + phoneNumber;
        } else {
            return "OTP verification failed for unsubscription.";
        }
    }
}
