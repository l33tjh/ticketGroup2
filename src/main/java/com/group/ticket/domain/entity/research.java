package com.group.ticket.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class research {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sid;

    @Column(length = 10, nullable = false)
    private String stype;

    @Column(length = 100, nullable = false)
    private String sname;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String sperformer;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime sdate;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String sexplain;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String splace;

    @Column(length = 255) // 길이 조절
    private String spath; // 추가

    @Builder
    public research(Long sid, String stype, String sname, String sperformer, String sexplain, String splace,String spath) {
        this.sid = sid;
        this.stype = stype;
        this.sname = sname;
        this.sperformer = sperformer;
        this.sexplain = sexplain;
        this.splace = splace;
        this.spath = spath;
    }
}
