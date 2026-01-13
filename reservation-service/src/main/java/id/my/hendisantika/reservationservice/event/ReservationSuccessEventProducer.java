package id.my.hendisantika.reservationservice.event;


import id.my.hendisantika.reservationservice.dto.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 11/01/26
 * Time: 14.54
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationSuccessEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishReservationSuccess(Notification notification) {
        kafkaTemplate.send("reservation.success", notification.getReservationId().toString(), notification);
        log.info("Event Published Successfully by reservationId={}", notification.getReservationId());
    }
}
