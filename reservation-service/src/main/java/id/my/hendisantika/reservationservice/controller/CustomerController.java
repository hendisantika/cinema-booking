package id.my.hendisantika.reservationservice.controller;

import id.my.hendisantika.reservationservice.dto.Customer;
import id.my.hendisantika.reservationservice.dto.response.CustomerResponseDTO;
import id.my.hendisantika.reservationservice.service.CustomerService;
import id.my.hendisantika.reservationservice.service.UserSyncService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@Valid @RequestBody Customer customer) {
        return customerService.updateCustomer(customer);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{email}")
    public ResponseEntity<CustomerResponseDTO> getCustomerByEmail(@PathVariable String email) {
        return customerService.getCustomerByEmail(email);
    }

    @DeleteMapping("/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerResponseDTO> deleteByEmail(@PathVariable String email) {
        return customerService.deleteCustomer(email);
    }

    @PostMapping("/sync")
    public ResponseEntity<String> syncUser(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No JWT found");
        }
        userSyncService.syncUser(jwt);

        return ResponseEntity.ok("User synced successfully");
    }
}
