package com.group.ticket.DAO;

import com.group.ticket.domain.entity.research;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResearchDAO {
    private Long sid;
    private String stype;
    private String sname;
    private String sperformer;
    private String sexplain;
    private LocalDateTime sdate;
    private String splace;
    private String spath;

    public research toEntity() {
        research researchBuild = research.builder()
                .sid(sid)
                .stype(stype)
                .sname(sname)
                .sperformer(sperformer)
                .sexplain(sexplain)
                .splace(splace)
                .spath(spath)  // 여기에 spath를 설정해줘야 합니다.
                .build();
        return researchBuild;
    }

    @Builder
    public ResearchDAO(Long sid, String stype, String sname, String sperformer, String sexplain, LocalDateTime sdate, String splace, String spath) {
        this.sid = sid;
        this.stype = stype;
        this.sname = sname;
        this.sperformer = sperformer;
        this.sexplain = sexplain;
        this.sdate = sdate;
        this.splace = splace;
        this.spath = spath;
    }
    public ResearchDAO(research entity) {
        this.sid = entity.getSid();
        this.stype = entity.getStype();
        this.sname = entity.getSname();
        this.sperformer = entity.getSperformer();
        this.sexplain = entity.getSexplain();
        this.sdate = entity.getSdate();
        this.splace = entity.getSplace();
        this.spath = entity.getSpath();
    }
}