package com.group.ticket.controller;

import com.group.ticket.service.ReservationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/bySid/{sid}")
    public List<String> getReservedSeats(@RequestParam Long sid) {
        // 서비스를 통해 특정 sid에 대한 예약된 좌석을 가져옴
        List<String> reservedSeats = reservationService.getReservedSeatsBySid(sid);
        return reservedSeats;
    }
}