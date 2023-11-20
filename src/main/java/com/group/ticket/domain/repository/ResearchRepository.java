package com.group.ticket.domain.repository;
import com.group.ticket.domain.entity.research;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResearchRepository extends JpaRepository<research, Long> {
    List<research> findByStypeContaining(String keyword);
    List<research> findBySnameContaining(String keyword);
    @Query("SELECT r FROM research r WHERE r.stype LIKE %:keyword% OR r.sname LIKE %:keyword% OR r.sperformer LIKE %:keyword% OR r.sexplain LIKE %:keyword% OR r.splace LIKE %:keyword%")
    List<research> findByKeywordContaining(@Param("keyword") String keyword);

    List<research> findBySpathContaining(String keyword);
}
