package id.my.hendisantika.paymentservice.listener;

import id.my.hendisantika.paymentservice.dto.PaymentRequest;
import id.my.hendisantika.paymentservice.dto.PaymentResponse;
import id.my.hendisantika.paymentservice.dto.PaymentSessionCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 14/01/26
 * Time: 07.02
 * To change this template use File | Settings | File Templates.
 */
@Component
@RequiredArgsConstructor
public class ReservationPendingListener {
    private final StripeService service;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "reservation.pending", groupId = "payment-service-group2")
    public void onReservationPending(PaymentRequest request) {
        PaymentResponse paymentResponse = service.checkoutReservation(request);

        kafkaTemplate.send(
                "payment.session.created",
                PaymentSessionCreatedEvent.builder()
                        .reservationId(request.getReservationId())
                        .sessionId(paymentResponse.getSessionId())
                        .sessionUrl(paymentResponse.getSessionUrl())
                        .build()
        );
    }
}
