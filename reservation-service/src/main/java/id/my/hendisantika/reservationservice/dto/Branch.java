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
 * Time: 07.58
 * To change this template use File | Settings | File Templates.
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Branch {
    private  Long id;
    private  String name;
    private  String location;
    private  String contact;
}
