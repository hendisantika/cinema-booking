package id.my.hendisantika.reservationservice.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 10/01/26
 * Time: 08.00
 * To change this template use File | Settings | File Templates.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Movie {
    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate showDate;
    private String description;
    private Long branchId;
}
