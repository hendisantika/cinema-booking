package id.my.hendisantika.reservationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 11/01/26
 * Time: 14.39
 * To change this template use File | Settings | File Templates.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSuccessEvent {
    private UUID reservationId;
    private String paymentIntentId;
    private Long amountTotal;
    private String currency;
}
