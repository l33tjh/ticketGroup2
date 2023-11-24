package com.group.ticket.domain.repository;

import com.group.ticket.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r.selectedSeats FROM Reservation r WHERE r.sid = :sid")
    List<String> findReservedSeatsBySid(@Param("sid") Long sid);
    List<Reservation> findByEmail(String email);
}
