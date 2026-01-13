package id.my.hendisantika.reservationservice.listener;

import id.my.hendisantika.reservationservice.dto.PaymentSuccessEvent;
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
 * Time: 14.59
 * To change this template use File | Settings | File Templates.
 */
@Component
@RequiredArgsConstructor
public class PaymentSuccessListener {

    private final ReservationService reservationService;

    @KafkaListener(topics = "payment.success")
    public void onPaymentSuccess(PaymentSuccessEvent event) {
        reservationService.confirmReservation(event.getReservationId());
    }
}
