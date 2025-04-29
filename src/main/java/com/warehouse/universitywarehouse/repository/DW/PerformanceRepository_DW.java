package com.warehouse.universitywarehouse.repository.DW;

import com.warehouse.universitywarehouse.model.DW.Performance_DW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository_DW extends JpaRepository<Performance_DW, Long> {
    List<Performance_DW> findByStudentId(Long studentId);
    List<Performance_DW> findByModuleId(Long moduleId);

    @Query(value = "SELECT COUNT(p) FROM Performance_DW p WHERE p.moduleId = :moduleId AND p.grade = 'F'")
    Integer countFailuresByModule(@Param("moduleId") Long moduleId);

    @Query(value = "SELECT COUNT(p) FROM Performance_DW p WHERE p.moduleId = :moduleId AND p.year = :year")
    Integer countByModuleAndYear(@Param("moduleId") Long moduleId, @Param("year") Integer year);

    @Query(value = "SELECT COUNT(p) FROM Performance_DW p WHERE p.moduleId = :moduleId AND p.grade = 'F' AND p.year = :year")
    Integer countFailuresByModuleAndYear(@Param("moduleId") Long moduleId, @Param("year") Integer year);
}
