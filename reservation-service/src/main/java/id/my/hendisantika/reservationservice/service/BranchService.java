package id.my.hendisantika.reservationservice.service;

import id.my.hendisantika.reservationservice.dto.Branch;
import id.my.hendisantika.reservationservice.entity.BranchEntity;
import id.my.hendisantika.reservationservice.repository.BranchRepository;
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
 * Time: 15.06
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<List<Branch>> getBranches() {
        try {
            List<BranchEntity> branchEntities = branchRepository.findAll();
            if (branchEntities.isEmpty()) {
                log.warn("No branches found for the add for the cinema");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            List<Branch> branchEntityList = branchEntities.stream()
                    .map(entity -> modelMapper.map(entity, Branch.class))
                    .toList();
            log.info("Branches list has been successfully retrieved");
            return ResponseEntity.status(HttpStatus.OK).body(branchEntityList);
        } catch (Exception ex) {
            log.error("Exception while finding Branch list: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
