package com.warehouse.universitywarehouse.service.OP;

import com.warehouse.universitywarehouse.model.OP.Program_OP;
import com.warehouse.universitywarehouse.repository.OP.ProgramRepository_OP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramService_OP {

    @Autowired
    private ProgramRepository_OP programRepositoryOP;

    public List<Program_OP> getAllPrograms() {
        return programRepositoryOP.findAll();
    }

    public Program_OP getProgramById(Long id) {
        return programRepositoryOP.findById(id).orElse(null);
    }

    public Program_OP createProgram(Program_OP programOP) {
        return programRepositoryOP.save(programOP);
    }

    public Program_OP updateProgram(Long id, Program_OP updatedProgramOP) {
        Program_OP existingProgramOP = programRepositoryOP.findById(id).orElse(null);
        if (existingProgramOP != null) {
            existingProgramOP.setProgramName(updatedProgramOP.getProgramName());
            existingProgramOP.setFacultyId(updatedProgramOP.getFacultyId());
            return programRepositoryOP.save(existingProgramOP);
        }
        return null;
    }

    public void deleteProgram(Long id) {
        programRepositoryOP.deleteById(id);
    }
}