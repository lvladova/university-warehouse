package com.warehouse.universitywarehouse.repository.DW;

import com.warehouse.universitywarehouse.model.DW.Admissions_DW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdmissionsRepository_DW extends JpaRepository<Admissions_DW, Long> {
    List<Admissions_DW> findByProgramId(Long programId);

    @Query(value = "SELECT COUNT(a) FROM Admissions_DW a WHERE a.programId = :programId AND EXTRACT(YEAR FROM a.admissionDate) = :year AND a.status = 'APPLIED'")
    Integer countApplicantsByProgramAndYear(Long programId, int year);

    @Query(value = "SELECT COUNT(a) FROM Admissions_DW a WHERE a.programId = :programId AND EXTRACT(YEAR FROM a.admissionDate) = :year AND a.status = 'ADMITTED'")
    Integer countAdmittedByProgramAndYear(Long programId, int year);
}