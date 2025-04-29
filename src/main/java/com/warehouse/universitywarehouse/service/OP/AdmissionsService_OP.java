package com.warehouse.universitywarehouse.service.OP;

import com.warehouse.universitywarehouse.model.OP.Admissions_OP;
import com.warehouse.universitywarehouse.repository.OP.AdmissionsRepository_OP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdmissionsService_OP {

    @Autowired
    private AdmissionsRepository_OP admissionsRepositoryOP;

    public List<Admissions_OP> getAllAdmissions() {
        return admissionsRepositoryOP.findAll();
    }

    public Admissions_OP getAdmissionsById(Long id) {
        return admissionsRepositoryOP.findById(id).orElse(null);
    }

    public Admissions_OP createAdmissions(Admissions_OP admissionsOP) {
        return admissionsRepositoryOP.save(admissionsOP);
    }

    public Admissions_OP updateAdmissions(Long id, Admissions_OP updatedAdmissionsOP) {
        Admissions_OP existingAdmissionsOP = admissionsRepositoryOP.findById(id).orElse(null);
        if (existingAdmissionsOP != null) {
            existingAdmissionsOP.setProgramId(updatedAdmissionsOP.getProgramId());
            existingAdmissionsOP.setAdmittedFlag(updatedAdmissionsOP.getAdmittedFlag());
            existingAdmissionsOP.setYear(updatedAdmissionsOP.getYear());
            return admissionsRepositoryOP.save(existingAdmissionsOP);
        }
        return null;
    }

    public void deleteAdmissions(Long id) {
        admissionsRepositoryOP.deleteById(id);
    }
}
