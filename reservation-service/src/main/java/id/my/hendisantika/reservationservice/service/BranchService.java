package id.my.hendisantika.reservationservice.service;

import id.my.hendisantika.reservationservice.dto.Branch;
import id.my.hendisantika.reservationservice.dto.response.BranchResponseDTO;
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

    public ResponseEntity<BranchResponseDTO> getBranch(Long branchId) {
        try {
            if (branchId == null) {
                log.warn("Branch id is null for the retrieve branches");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            if (branchRepository.existsById(branchId)) {
                log.info("Branch found with id {}", branchId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new BranchResponseDTO("Branch found successfully", modelMapper
                                .map(branchRepository.findById(branchId), Branch.class)));
            }
            log.info("Branch not found with id {}", branchId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (Exception ex) {
            log.error("Exception while finding Branch: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<BranchResponseDTO> addBranch(Branch branch) {
        try {
            if (branch == null) {
                log.warn("Branch id is null for the add for the cinema");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            if (branchRepository.existsByContact((branch.getContact()))) {
                log.warn("Branch already exists with id {}", branch.getId());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            BranchEntity branchEntity = branchRepository.save(modelMapper.map(branch, BranchEntity.class));
            log.info("Branch has  been successfully added");
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new BranchResponseDTO("Branch " + branchEntity.getId() + " has been Successfully added",
                            modelMapper.map(branchEntity, Branch.class)));

        } catch (Exception ex) {
            log.error("Exception while finding Branch: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<BranchResponseDTO> updateBranch(Branch branch) {
        try {
            if (branch == null) {
                log.warn("Branch id is null for the update for the cinema");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            if (branchRepository.existsById(branch.getId())) {
                BranchEntity branchEntity = branchRepository.save(modelMapper.map(branch, BranchEntity.class));
                log.info("Branch found with id {} for the update", branchEntity.getId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new BranchResponseDTO("Branch " + branchEntity.getId() + "found successfully updated",
                                modelMapper.map(branchEntity, Branch.class)));

            }
            log.info("Branch not found with id {} for the update", branch.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new BranchResponseDTO("Branch " + branch.getId() + "not found for th update", null));

        } catch (Exception ex) {
            log.error("Exception while finding Branch: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
