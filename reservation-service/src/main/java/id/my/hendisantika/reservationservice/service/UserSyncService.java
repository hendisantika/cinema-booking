package id.my.hendisantika.reservationservice.service;

import id.my.hendisantika.reservationservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 13/01/26
 * Time: 06.29
 * To change this template use File | Settings | File Templates.
 */
@Service
@RequiredArgsConstructor
public class UserSyncService {

    private final CustomerRepository customerRepository;

}
