package com.warehouse.universitywarehouse.service.OP;

import com.warehouse.universitywarehouse.model.OP.Finance_OP;
import com.warehouse.universitywarehouse.repository.OP.FinanceRepository_OP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceService_OP {

    @Autowired
    private FinanceRepository_OP financeRepositoryOP;

    public List<Finance_OP> getAllFinances() {
        return financeRepositoryOP.findAll();
    }

    public Finance_OP getFinanceById(Long id) {
        return financeRepositoryOP.findById(id).orElse(null);
    }

    public Finance_OP createFinance(Finance_OP financeOP) {
        return financeRepositoryOP.save(financeOP);
    }

    public Finance_OP updateFinance(Long id, Finance_OP updatedFinanceOP) {
        Finance_OP existingFinanceOP = financeRepositoryOP.findById(id).orElse(null);
        if (existingFinanceOP != null) {
            existingFinanceOP.setStudentId(updatedFinanceOP.getStudentId());
            existingFinanceOP.setFacultyId(updatedFinanceOP.getFacultyId());
            existingFinanceOP.setAmountPaid(updatedFinanceOP.getAmountPaid());
            existingFinanceOP.setScholarshipFlag(updatedFinanceOP.getScholarshipFlag());
            existingFinanceOP.setYear(updatedFinanceOP.getYear());
            return financeRepositoryOP.save(existingFinanceOP);
        }
        return null;
    }

    public void deleteFinance(Long id) {
        financeRepositoryOP.deleteById(id);
    }
}
