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

import java.util.List;

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

    public ResponseEntity<CustomerResponseDTO> updateCustomer(Customer customer) {
        try {
            if (customer == null) {
                log.error("Customer is null for update");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new CustomerResponseDTO("Customer is null", null));
            }
            if (customerRepository.existsById(customer.getId())) {
                CustomerEntity customerEntity = customerRepository.findById(customer.getId()).orElse(null);
                if (customerEntity != null) {
                    customerEntity.setAddress(customer.getAddress());
                    customerEntity.setNumber(customer.getNumber());
                    customerRepository.save(customerEntity);
                    log.info("Customer with email {} updated Successfully", customerEntity.getEmail());
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new CustomerResponseDTO("Customer with email " + customerEntity.getEmail() + " updated successfully",
                                    modelMapper.map(customerEntity, Customer.class)));
                }
            }
            log.warn("Customer with email {} not exits for the update", customer.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new CustomerResponseDTO("Customer with email " + customer.getEmail() + " not exits", customer));

        } catch (Exception ex) {
            log.error("Exception while updating user: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomerResponseDTO("An exception occurred: " + ex.getMessage(), null));
        }
    }

    public ResponseEntity<CustomerResponseDTO> deleteCustomer(String email) {
        try {
            if (email == null || email.isEmpty()) {
                log.error("Email is null for the delete");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new CustomerResponseDTO("Email is null", null));
            }
            if (customerRepository.existsByEmail(email)) {
                customerRepository.delete(modelMapper.map(email, CustomerEntity.class));
                log.info("Customer with email {} deleted Successfully", email);
                ResponseEntity.status(HttpStatus.OK)
                        .body(new CustomerResponseDTO("Customer with email " + email + " deleted Successfully", null));
            }
            log.warn("Customer with email {} not exits for the delete", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomerResponseDTO("Customer with email " + email + " not exits", null));
        } catch (Exception ex) {
            log.error("Exception while deleting user: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomerResponseDTO("An exception occurred: " + ex.getMessage(), null));
        }
    }

    public ResponseEntity<List<Customer>> getAllCustomer() {
        try {
            List<CustomerEntity> customerEntities = customerRepository.findAll();

            if (customerEntities.isEmpty()) {
                log.warn("No customers found");
                return ResponseEntity.noContent().build();
            }
            List<Customer> customers = customerEntities.stream()
                    .map(entity -> modelMapper.map(entity, Customer.class))
                    .toList();
            log.info("Fetched all customers successfully, count: {}", customers.size());
            return ResponseEntity.ok(customers);

        } catch (Exception ex) {
            log.error("Exception while finding user: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }
}
