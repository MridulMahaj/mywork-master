package VASService.mywork.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    private int id;
    private int userId;
    private String userPhoneNumber;
    private String status;
    private LocalDateTime subscribedAt;
    private LocalDateTime unsubscribedAt;
    private LocalDateTime lastUpdated;
}
