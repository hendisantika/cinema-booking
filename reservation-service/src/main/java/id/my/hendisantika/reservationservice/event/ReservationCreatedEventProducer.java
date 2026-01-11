package id.my.hendisantika.reservationservice.event;

import id.my.hendisantika.reservationservice.dto.PaymentDetails;
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
public class ReservationCreatedEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishReservationCreated(PaymentDetails paymentDetails) {
        kafkaTemplate.send("reservation.pending", paymentDetails.getReservationId().toString(), paymentDetails);
        log.info("Event Published Successfully by reservationId={}", paymentDetails.getReservationId());
    }
}
