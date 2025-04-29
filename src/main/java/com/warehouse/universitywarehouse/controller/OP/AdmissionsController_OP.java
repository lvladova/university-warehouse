package com.warehouse.universitywarehouse.controller.OP;

import com.warehouse.universitywarehouse.model.OP.Admissions_OP;
import com.warehouse.universitywarehouse.service.OP.AdmissionsService_OP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admissions")
public class AdmissionsController_OP {

    @Autowired
    private AdmissionsService_OP admissionsServiceOP;

    @GetMapping
    public List<Admissions_OP> getAllAdmissions() {
        return admissionsServiceOP.getAllAdmissions();
    }

    @GetMapping("/{id}")
    public Admissions_OP getAdmissionsById(@PathVariable Long id) {
        return admissionsServiceOP.getAdmissionsById(id);
    }

    @PostMapping
    public Admissions_OP createAdmissions(@RequestBody Admissions_OP admissionsOP) {
        return admissionsServiceOP.createAdmissions(admissionsOP);
    }

    @PutMapping("/{id}")
    public Admissions_OP updateAdmissions(@PathVariable Long id, @RequestBody Admissions_OP admissionsOP) {
        return admissionsServiceOP.updateAdmissions(id, admissionsOP);
    }

    @DeleteMapping("/{id}")
    public String deleteAdmissions(@PathVariable Long id) {
        admissionsServiceOP.deleteAdmissions(id);
        return "Admissions record with ID " + id + " deleted.";
    }
}
