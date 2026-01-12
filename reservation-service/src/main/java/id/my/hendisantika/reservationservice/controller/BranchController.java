package id.my.hendisantika.reservationservice.controller;

import id.my.hendisantika.reservationservice.dto.Branch;
import id.my.hendisantika.reservationservice.dto.response.BranchResponseDTO;
import id.my.hendisantika.reservationservice.service.BranchService;
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
 * Time: 06.46
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/branch")
public class BranchController {
    private final BranchService branchService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Branch>> getAllBranches() {
        return branchService.getBranches();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BranchResponseDTO> createBranch(@RequestBody Branch branch) {
        return branchService.addBranch(branch);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<BranchResponseDTO> getBranchById(@PathVariable Long id) {
        return branchService.getBranch(id);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BranchResponseDTO> updateBranch(@RequestBody Branch branch) {
        return branchService.updateBranch(branch);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BranchResponseDTO> deleteBranchById(@PathVariable Long id) {
        return branchService.deleteBranch(id);
    }
}
