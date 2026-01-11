package id.my.hendisantika.reservationservice.service;

import id.my.hendisantika.reservationservice.dto.Customer;
import id.my.hendisantika.reservationservice.dto.response.CustomerResponseDTO;
import id.my.hendisantika.reservationservice.entity.CustomerEntity;
import id.my.hendisantika.reservationservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 11/01/26
 * Time: 15.10
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<CustomerResponseDTO> getCustomerByEmail(String email) {
        try {
            if (email == null || email.isEmpty()) {
                log.error("email is null or email is empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            if (customerRepository.existsByEmail(email)) {
                log.info("Customer with email {} found", email);
                CustomerEntity customerEntity = customerRepository.findByEmail(email);
                return ResponseEntity.status(HttpStatus.FOUND)
                        .body(new CustomerResponseDTO("Customer", modelMapper.map(customerEntity, Customer.class)));

            }
            log.warn("Customer with email {} not exits", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomerResponseDTO("Customer with email " + email + " not exits", null));
        } catch (Exception ex) {
            log.error("Exception while saving user: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomerResponseDTO("An exception occurred: " + ex.getMessage(), null));
        }
    }

    public ResponseEntity<CustomerResponseDTO> setCustomer(Customer customer) {
        try {
            if (customer == null) {
                log.error("Customer is null for the add");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new CustomerResponseDTO("Customer is null", null));
            }
            if (!customerRepository.existsByEmail(customer.getEmail())) {
                customerRepository.save(modelMapper.map(customer, CustomerEntity.class));
                log.info("Customer with email {} saved Successfully", customer.getEmail());
                ResponseEntity.status(HttpStatus.CREATED)
                        .body(new CustomerResponseDTO("Customer with email " + customer.getEmail() + " saved successfully", customer));

            }
            log.warn("Customer with email {} already exits", customer.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new CustomerResponseDTO("Customer with email " + customer.getEmail() + " already exits", customer));
        } catch (Exception ex) {
            log.error("Exception while saving user: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomerResponseDTO("An exception occurred: " + ex.getMessage(), null));
        }
    }
}
