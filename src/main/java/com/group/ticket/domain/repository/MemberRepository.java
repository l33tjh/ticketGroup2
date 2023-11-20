package com.group.ticket.domain.repository;

import com.group.ticket.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    boolean existsByMemberEmail(String memberEmail);

    Optional<MemberEntity> findByMemberEmail(String memberEmail);

    Optional<MemberEntity> findByMno(Long Mno);


    //MemberEntity save(MemberEntity memberEntity);
    /*Optional<MemberEntity> findByMno(Long Mno);
    Optional<MemberEntity> findByMId(String Mid);
    Optional<MemberEntity> findByMEmail(String Memail);

    Optional<MemberEntity> findByMName(String Mname);

    List<MemberEntity> findAll();*/

}