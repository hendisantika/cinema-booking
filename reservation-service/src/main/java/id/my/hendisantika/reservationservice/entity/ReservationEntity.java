package id.my.hendisantika.reservationservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import id.my.hendisantika.reservationservice.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 11/01/26
 * Time: 14.48
 * To change this template use File | Settings | File Templates.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "reservation")
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "reservation_id", nullable = false, updatable = false)
    private UUID reservationId;

    @Column(name = "con_number",length = 10, nullable = false)
    private String conNumber;

    @Column(name = "description")
    private String description;

    @Column(name = "reservation_date",nullable = false)
    private LocalDate date;

    @Column(name = "reservation_time",nullable = false)
    private LocalTime time;

    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "session_url",length = 2000)
    private String sessionUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookedBy", nullable = false)
    @JsonBackReference
    private CustomerEntity customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id",nullable = false)
    @JsonBackReference
    private CinemaEntity cinema;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    @JsonBackReference
    private MovieEntity movie;

    @ManyToMany(mappedBy = "reservation",fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<SeatEntity> seats=new HashSet<>();

}
