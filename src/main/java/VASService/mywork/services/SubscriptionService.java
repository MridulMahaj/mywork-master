package VASService.mywork.services;

import VASService.mywork.classes.Subscription;
import VASService.mywork.websocket.SubscriptionWebSocket;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class SubscriptionService {

    private final JdbcTemplate jdbcTemplate;
    private final SubscriptionWebSocket subscriptionWebSocket;

    public SubscriptionService(JdbcTemplate jdbcTemplate, SubscriptionWebSocket subscriptionWebSocket) {
        this.jdbcTemplate = jdbcTemplate;
        this.subscriptionWebSocket = subscriptionWebSocket;
    }

    private Integer ensureUserAndGetId(String userPhoneNumber) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT id FROM users WHERE phone_number = ?",
                    Integer.class,
                    userPhoneNumber
            );
        } catch (EmptyResultDataAccessException e) {
            jdbcTemplate.update("INSERT INTO users (phone_number) VALUES (?)", userPhoneNumber);
            return jdbcTemplate.queryForObject(
                    "SELECT id FROM users WHERE phone_number = ?",
                    Integer.class,
                    userPhoneNumber
            );
        }
    }

    private Subscription mapToSubscription(ResultSet rs, int rowNum) throws SQLException {
        return new Subscription(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("user_phone_number"),
                rs.getString("status"),
                rs.getTimestamp("subscribed_at") != null ? rs.getTimestamp("subscribed_at").toLocalDateTime() : null,
                rs.getTimestamp("unsubscribed_at") != null ? rs.getTimestamp("unsubscribed_at").toLocalDateTime() : null,
                rs.getTimestamp("last_updated") != null ? rs.getTimestamp("last_updated").toLocalDateTime() : null
        );
    }

    private Subscription getSubscriptionByUserId(Integer userId) {
        return jdbcTemplate.query(
                "SELECT * FROM subscriptions WHERE user_id = ?",
                this::mapToSubscription,
                userId
        ).stream().findFirst().orElse(null);
    }

    public Subscription markPending(String userPhoneNumber) {
        Integer userId = ensureUserAndGetId(userPhoneNumber);

        jdbcTemplate.update(
                "INSERT INTO subscriptions (user_id, user_phone_number, status) VALUES (?, ?, 'PENDING') " +
                        "ON DUPLICATE KEY UPDATE status='PENDING', last_updated=NOW()",
                userId, userPhoneNumber
        );

        Subscription subscription = getSubscriptionByUserId(userId);
        subscriptionWebSocket.broadcastStatus(subscription);
        return subscription;
    }

    public Subscription subscribeUser(String userPhoneNumber) {
        Integer userId = ensureUserAndGetId(userPhoneNumber);

        jdbcTemplate.update(
                "INSERT INTO subscriptions (user_id, user_phone_number, status, subscribed_at) " +
                        "VALUES (?, ?, 'SUBSCRIBED', NOW()) " +
                        "ON DUPLICATE KEY UPDATE status='SUBSCRIBED', subscribed_at=NOW(), last_updated=NOW()",
                userId, userPhoneNumber
        );

        Subscription subscription = getSubscriptionByUserId(userId);
        subscriptionWebSocket.broadcastStatus(subscription);
        return subscription;
    }

    public Subscription unsubscribeUser(String userPhoneNumber) {
        Integer userId = ensureUserAndGetId(userPhoneNumber);

        jdbcTemplate.update(
                "INSERT INTO subscriptions (user_id, user_phone_number, status, unsubscribed_at) " +
                        "VALUES (?, ?, 'UNSUBSCRIBED', NOW()) " +
                        "ON DUPLICATE KEY UPDATE status='UNSUBSCRIBED', unsubscribed_at=NOW(), last_updated=NOW()",
                userId, userPhoneNumber
        );

        Subscription subscription = getSubscriptionByUserId(userId);
        subscriptionWebSocket.broadcastStatus(subscription);
        return subscription;
    }
}
