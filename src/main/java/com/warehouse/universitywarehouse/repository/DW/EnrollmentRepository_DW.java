package com.warehouse.universitywarehouse.repository.DW;

import com.warehouse.universitywarehouse.model.DW.Enrollment_DW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository_DW extends JpaRepository<Enrollment_DW, Long> {
    List<Enrollment_DW> findByStudentId(Long studentId);

    @Query(value = "SELECT COUNT(e) FROM Enrollment_DW e JOIN Student_DW s ON e.studentId = s.studentId WHERE EXTRACT(YEAR FROM e.enrollmentDate) = :year")
    Integer countEnrollmentsByYear(int year);

    @Query(value = "SELECT COUNT(e) FROM Enrollment_DW e JOIN Student_DW s ON e.studentId = s.studentId WHERE s.nationality = :nationality AND EXTRACT(YEAR FROM e.enrollmentDate) = :year")
    Integer countEnrollmentsByNationalityAndYear(String nationality, int year);
}
