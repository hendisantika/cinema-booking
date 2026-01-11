package id.my.hendisantika.reservationservice.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    private UUID reservationId;
    @Size(min = 10, max = 10)
    private String conNumber;
    private String description;
    private LocalDate date;
    private LocalTime time;
    private Long customerId;
    private Long cinemaId;
    private Long  movieId;
    private List<Long> seatIds;
}
