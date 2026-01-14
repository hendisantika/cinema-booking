package id.my.hendisantika.paymentservice.controller;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import id.my.hendisantika.paymentservice.dto.PaymentFailedEvent;
import id.my.hendisantika.paymentservice.dto.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 14/01/26
 * Time: 07.05
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping("/stripe/webhook")
@RequiredArgsConstructor
public class PaymentController {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${stripe.webhookSecret}")
    private String webhookSecret;

    @PostMapping
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader

    ) {
        Event event;

        try {
            event = Webhook.constructEvent(
                    payload,
                    sigHeader,
                    webhookSecret
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        }
        switch (event.getType()) {

            case "checkout.session.completed" -> {
                Session session = (Session) event.getDataObjectDeserializer()
                        .getObject().orElseThrow();

                String reservationIdStr = session.getMetadata().get("reservationId");

                if (reservationIdStr == null || reservationIdStr.isBlank()) {
                    throw new IllegalStateException("Missing reservationId in Stripe metadata");
                }
                UUID reservationId = UUID.fromString(reservationIdStr);

                kafkaTemplate.send(
                        "payment.success",
                        new PaymentSuccessEvent(
                                reservationId,
                                session.getPaymentIntent(),
                                session.getAmountTotal(),
                                session.getCurrency()
                        )
                );
            }

            case "payment_intent.payment_failed",
                 "checkout.session.async_payment_failed" -> {

                PaymentIntent intent = (PaymentIntent) event
                        .getDataObjectDeserializer()
                        .getObject().orElseThrow();

                String reservationIdStr = intent.getMetadata().get("reservationId");

                UUID reservationId = UUID.fromString(reservationIdStr);

                kafkaTemplate.send(
                        "payment.failed",
                        new PaymentFailedEvent(
                                reservationId,
                                intent.getId(),
                                intent.getLastPaymentError() != null
                                        ? intent.getLastPaymentError().getMessage()
                                        : "PAYMENT_FAILED"
                        )
                );
            }
        }
        return ResponseEntity.ok("webhook processed");
    }
}
