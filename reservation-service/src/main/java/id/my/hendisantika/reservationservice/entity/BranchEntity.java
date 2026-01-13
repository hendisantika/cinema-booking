package id.my.hendisantika.reservationservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 11/01/26
 * Time: 14.45
 * To change this template use File | Settings | File Templates.
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "branch")
public class BranchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bran_id")
    private  Long id;

    @Column(name = "bran_name", nullable = false)
    private  String name;

    @Column(name = "bran_location", nullable = false)
    private  String location;

    @Column(name = "bran_contact", nullable = false)
    private  String contact;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonBackReference
    private Set<CinemaEntity> cinemas=new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "movie_branch",
            joinColumns = @JoinColumn(name = "branch_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    @JsonManagedReference
    private Set<MovieEntity> movies=new HashSet<>();
}
