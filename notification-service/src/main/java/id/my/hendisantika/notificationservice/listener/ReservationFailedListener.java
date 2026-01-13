package id.my.hendisantika.notificationservice.listener;

import id.my.hendisantika.notificationservice.dto.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 14/01/26
 * Time: 06.48
 * To change this template use File | Settings | File Templates.
 */
@Component
@RequiredArgsConstructor
public class ReservationFailedListener {
    private final EmailService emailService;

    @KafkaListener(topics = "reservation.Created", groupId = "notification-service-group2")
    public void onReservationCreated(Notification notification) {

        String email = notification.getEmail();
        String subject = "Notification Created";
        String content =
                "Hello " + notification.getCustomerName() + ",\n\n" +
                        "Your notification has been successfully created.\n" +
                        "Notification ID: " + notification.getReservationId() + "\n\n" +
                        "Thank you.";
        emailService.sendEmail(email, subject, content);

    }
}
