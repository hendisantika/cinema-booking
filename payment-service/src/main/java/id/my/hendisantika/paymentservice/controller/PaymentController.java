package id.my.hendisantika.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
