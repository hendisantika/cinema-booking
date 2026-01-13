package id.my.hendisantika.reservationservice.service;

import id.my.hendisantika.reservationservice.entity.CustomerEntity;
import id.my.hendisantika.reservationservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
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

    public void syncUser(Jwt jwt) {
        String keycloakId = jwt.getSubject(); // sub
        String email = jwt.getClaimAsString("email");
        String username = jwt.getClaimAsString("preferred_username");

        String age = jwt.getClaim("age");
        String address = jwt.getClaimAsString("address");
        String number = jwt.getClaimAsString("number");

        customerRepository.findByKeyClockId(keycloakId)
                .ifPresentOrElse(
                        existing -> System.out.println("✅ User already exists"),
                        () -> {
                            CustomerEntity customer = new CustomerEntity();
                            customer.setKeyClockId(keycloakId);
                            customer.setEmail(email);
                            customer.setName(username);
                            customer.setAge(age);
                            customer.setAddress(address);
                            customer.setNumber(number);
                            customerRepository.save(customer);
                        }
                );
    }
}
