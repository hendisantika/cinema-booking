package id.my.hendisantika.reservationservice.dto.response;

import id.my.hendisantika.reservationservice.dto.Reservation;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 11/01/26
 * Time: 14.43
 * To change this template use File | Settings | File Templates.
 */
public record ReservationResponseDTO(String message, Reservation reservation) {
}
