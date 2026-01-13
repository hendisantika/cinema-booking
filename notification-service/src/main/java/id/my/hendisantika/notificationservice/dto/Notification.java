package id.my.hendisantika.notificationservice.dto;

import lombok.Data;

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
 * Date: 14/01/26
 * Time: 06.44
 * To change this template use File | Settings | File Templates.
 */
@Data
public class Notification {
    private UUID reservationId;
    private LocalDate date;
    private LocalTime time;
    private String customerName;
    private String email;
    private String branchName;
    private Long cinemaId;
    private String movieName;
    private List<Long> seatIds;
}
