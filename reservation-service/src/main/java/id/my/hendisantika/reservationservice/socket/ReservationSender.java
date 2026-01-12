package id.my.hendisantika.reservationservice.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.my.hendisantika.reservationservice.dto.response.ReservationResponseDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 13/01/26
 * Time: 06.31
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationSender extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        sessions.add(session);
        log.info("Connected to web socket session {}", session.getId());
    }

    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) {
        sessions.remove(session);
        log.info("Disconnected from web socket session {}", session.getId());
    }

    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        log.info("Received message from web socket session {}", session.getId());
    }

    public void broadcast(ReservationResponseDTO responseDTO) {
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    try {
                        String jsonString = objectMapper.writeValueAsString(responseDTO);
                        session.sendMessage(new TextMessage(jsonString));
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                    }
                }
            }
        }
    }
}
