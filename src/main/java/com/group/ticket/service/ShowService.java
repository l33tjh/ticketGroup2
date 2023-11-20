//package com.group.ticket.service;
//
//import com.group.ticket.domain.entity.Show;
//import com.group.ticket.domain.repository.ShowRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class ShowService {
//    @Autowired
//    private ShowRepository showRepository; // JPA 레포지토리
//
//    public List<Show> getAllConcerts() {
//        return showRepository.findAll();
//    }
//}