package com.warehouse.universitywarehouse.repository.DW;

import com.warehouse.universitywarehouse.model.DW.Finance_DW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceRepository_DW extends JpaRepository<Finance_DW, Long> {

    @Query(value = "SELECT SUM(f.amount) FROM Finance_DW f WHERE EXTRACT(YEAR FROM f.transactionDate) = :year")
    Double getTotalAmountByYear(int year);

    @Query(value = "SELECT SUM(f.amount) FROM Finance_DW f JOIN Student_DW s ON f.studentId = s.studentId " +
            "WHERE EXTRACT(YEAR FROM f.transactionDate) = :year AND s.nationality = :nationality")
    Double getTotalAmountByYearAndNationality(int year, String nationality);

    @Query(value = "SELECT AVG(f.amount) FROM Finance_DW f WHERE EXTRACT(YEAR FROM f.transactionDate) = :year")
    Double getAverageAmountByYear(int year);
}