package id.my.hendisantika.reservationservice.listener;


import id.my.hendisantika.reservationservice.dto.PaymentFailedEvent;
import id.my.hendisantika.reservationservice.service.ReservationService;
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
public class PaymentFailedListener {

    private final ReservationService reservationService;

    @KafkaListener(topics = "payment.failed")
    public void onPaymentFailed(PaymentFailedEvent event) {
        reservationService.failReservation(event.getReservationId());
    }
}
