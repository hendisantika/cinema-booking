package id.my.hendisantika.paymentservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 14/01/26
 * Time: 07.03
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Service
public class StripeService {

    @Value("${stripe.secretKey}")
    private String secretKey;
}
