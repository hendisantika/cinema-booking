package id.my.hendisantika.paymentservice.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import id.my.hendisantika.paymentservice.dto.PaymentRequest;
import id.my.hendisantika.paymentservice.dto.PaymentResponse;
import id.my.hendisantika.paymentservice.dto.SeatDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

    public PaymentResponse checkoutReservation(PaymentRequest paymentRequest) {
        Stripe.apiKey = secretKey;

        try {

            // 4️⃣ Checkout session builder
            SessionCreateParams.Builder sessionBuilder =
                    SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setSuccessUrl("http://localhost:8082/success")
                            .setCancelUrl("http://localhost:8082/cancel")
                            .putMetadata("reservationId", paymentRequest.getReservationId().toString());


            // LOOP: one LineItem per seat type
            for (SeatDetail seat : paymentRequest.getSeats()) {

                // 1️⃣ Product data (WHAT is being sold)
                SessionCreateParams.LineItem.PriceData.ProductData productData =
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(seat.getType() + " Seat")
                                .build();

                long unitAmount = BigDecimal.valueOf(seat.getPrice())
                        .multiply(BigDecimal.valueOf(100))
                        .longValueExact();

                // 2️⃣ Price data (HOW MUCH + currency)
                SessionCreateParams.LineItem.PriceData priceData =
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(
                                        paymentRequest.getCurrency() == null
                                                ? "usd"
                                                : paymentRequest.getCurrency().toLowerCase()
                                )
                                .setUnitAmount(unitAmount)// cents
                                .setProductData(productData)
                                .build();

                // 3️⃣ Line item (HOW MANY)
                SessionCreateParams.LineItem lineItem =
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(Long.valueOf(seat.getQuantity()))
                                .setPriceData(priceData)
                                .build();

                // add line item to session
                sessionBuilder.addLineItem(lineItem);
            }

            // 5️⃣ Create Stripe session
            Session session = Session.create(sessionBuilder.build());

            // 6️⃣ Return success ONLY if Stripe succeeded
            // log.info("......Payment Successfully .......");
            return PaymentResponse.builder()
                    .status("SUCCESS")
                    .message("Payment session created")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();

        } catch (StripeException e) {
            throw new RuntimeException(
                    "Stripe checkout session creation failed: " + e.getMessage(), e
            );
        }
    }
}
