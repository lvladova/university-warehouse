package com.warehouse.universitywarehouse.controller.OP;

import com.warehouse.universitywarehouse.model.OP.Finance_OP;
import com.warehouse.universitywarehouse.service.OP.FinanceService_OP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finances")
public class FinanceController_OP {

    @Autowired
    private FinanceService_OP financeServiceOP;

    @GetMapping
    public List<Finance_OP> getAllFinances() {
        return financeServiceOP.getAllFinances();
    }

    @GetMapping("/{id}")
    public Finance_OP getFinanceById(@PathVariable Long id) {
        return financeServiceOP.getFinanceById(id);
    }

    @PostMapping
    public Finance_OP createFinance(@RequestBody Finance_OP financeOP) {
        return financeServiceOP.createFinance(financeOP);
    }

    @PutMapping("/{id}")
    public Finance_OP updateFinance(@PathVariable Long id, @RequestBody Finance_OP financeOP) {
        return financeServiceOP.updateFinance(id, financeOP);
    }

    @DeleteMapping("/{id}")
    public String deleteFinance(@PathVariable Long id) {
        financeServiceOP.deleteFinance(id);
        return "Finance record with ID " + id + " deleted.";
    }
}
