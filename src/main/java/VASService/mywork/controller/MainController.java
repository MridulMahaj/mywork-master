package VASService.mywork.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MainController {

    private final RestTemplate restTemplate = new RestTemplate();

    // Cricket Service
    @GetMapping("/cricket")
    public String getCricketData() {
        String url = "http://localhost:8083/matches/live";
        return restTemplate.getForObject(url, String.class);
    }

    /*
    // Movie Service
    @GetMapping("/movies")
    public String getMovieData() {
        String url = "http://localhost:8084/api/movies";
        return restTemplate.getForObject(url, String.class);
    }*/

    /*

    // Tetris Service
    @GetMapping("/tetris")
    public String getTetrisData() {
        String url = "http://localhost:8085/api/tetris";
        return restTemplate.getForObject(url, String.class);
    }
    */
    // Weather Service
    @GetMapping("/weather")
    public String getWeatherData(@RequestParam String city) {
        String url = "http://localhost:8082/api/weather/current?city=" + city;
        return restTemplate.getForObject(url, String.class);
    }

    // News Service
    @GetMapping("/news")
    public String getNewsData(@RequestParam String story) {
        String url = "http://localhost:8081/api/news/full-story?story=" + story;
        return restTemplate.getForObject(url, String.class);
    }
}
