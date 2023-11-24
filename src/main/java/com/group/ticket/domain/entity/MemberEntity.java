package com.group.ticket.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Table(name = "member")//database에 해당 이름의 테이블 생성
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberEntity { //table 역할
    //jpa ==> database를 객체처럼 사용 가능

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;
    @Column
    private String memberPassword;
    @Column
    private String memberName;
    @Column(unique = true)
    private String memberEmail;

    private UserRole role;

    public enum UserRole {
        USER, ADMIN;
    }
    public Object getLoginUser() {
        return null;
    }

    public static MemberEntity toMemberEntity(MemberEntity memberEnt){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberEnt.getMemberEmail());
        memberEntity.setMemberName(memberEnt.getMemberName());
        memberEntity.setMemberPassword(memberEnt.getMemberPassword());
        return memberEntity;
    }

}