package id.my.hendisantika.reservationservice.controller;

import id.my.hendisantika.reservationservice.dto.Customer;
import id.my.hendisantika.reservationservice.dto.response.CustomerResponseDTO;
import id.my.hendisantika.reservationservice.service.CustomerService;
import id.my.hendisantika.reservationservice.service.UserSyncService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 13/01/26
 * Time: 07.08
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final UserSyncService userSyncService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomer() {
        return customerService.getAllCustomer();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CustomerResponseDTO> setCustomer(@Valid @RequestBody Customer customer) {
        return customerService.setCustomer(customer);
    }

}
