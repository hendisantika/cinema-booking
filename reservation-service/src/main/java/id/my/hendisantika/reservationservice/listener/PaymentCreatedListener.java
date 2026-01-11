package id.my.hendisantika.reservationservice.listener;

import id.my.hendisantika.reservationservice.dto.PaymentSessionCreatedEvent;
import id.my.hendisantika.reservationservice.entity.ReservationEntity;
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
 * Date: 11/01/26
 * Time: 14.56
 * To change this template use File | Settings | File Templates.
 */
@Component
@RequiredArgsConstructor
public class PaymentCreatedListener {

    private final ReservationService service;

    @KafkaListener(topics = "payment.session.created", groupId = "reservation-service-group")
    public void onPaymentCreated(PaymentSessionCreatedEvent paymentSessionCreatedEvent) {
        ReservationEntity reservationEntity = service.updateSession(paymentSessionCreatedEvent);
    }
}
