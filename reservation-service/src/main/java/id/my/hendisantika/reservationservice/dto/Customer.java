package id.my.hendisantika.reservationservice.dto;

import lombok.*;

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
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private Long id;
    private String keyClockId;
    private String name;
    private String email;
    private String address;
    private String number;
    private String age;
}
