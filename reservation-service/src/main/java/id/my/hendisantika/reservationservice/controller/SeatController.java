package id.my.hendisantika.reservationservice.controller;

import id.my.hendisantika.reservationservice.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 13/01/26
 * Time: 07.55
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping("/api/seat")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

}
