package id.my.hendisantika.reservationservice.config;

import id.my.hendisantika.reservationservice.socket.ReservationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 13/01/26
 * Time: 06.43
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final ReservationSender reservationSender;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(reservationSender, "/status").setAllowedOrigins("*");
    }
}
