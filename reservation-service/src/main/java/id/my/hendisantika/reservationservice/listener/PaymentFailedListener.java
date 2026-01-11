package id.my.hendisantika.reservationservice.listener;

import org.springframework.kafka.annotation.KafkaListener;

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
