package id.my.hendisantika.reservationservice.dto;

import id.my.hendisantika.reservationservice.enums.SeatType;
import lombok.*;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 11/01/26
 * Time: 14.40
 * To change this template use File | Settings | File Templates.
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    private Long id;
    private String number;
    private String rowLetter;
    private SeatType type;
    private Double price;
    private boolean isAvailable;
    private Long cinemaId;

}
