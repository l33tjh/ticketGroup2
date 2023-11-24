package com.group.ticket.service;

import com.group.ticket.domain.entity.Reservation;
import com.group.ticket.domain.repository.MemberRepository;
import com.group.ticket.domain.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
    }

    public List<String> getReservedSeatsBySid(Long sid) {
        // 특정 sid에 대한 예약된 좌석을 데이터베이스에서 가져옴
        return reservationRepository.findReservedSeatsBySid(sid);
    }

    public List<Reservation> getTicketsByEmail(String email) {
        // 사용자 ID를 기반으로 티켓 목록을 조회하는 로직
        return reservationRepository.findByEmail(email);
    }

}