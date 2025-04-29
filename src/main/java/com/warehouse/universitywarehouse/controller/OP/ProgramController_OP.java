package com.warehouse.universitywarehouse.controller.OP;

import com.warehouse.universitywarehouse.model.OP.Program_OP;
import com.warehouse.universitywarehouse.service.OP.ProgramService_OP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programs")
public class ProgramController_OP {

    @Autowired
    private ProgramService_OP programServiceOP;

    @GetMapping
    public List<Program_OP> getAllPrograms() {
        return programServiceOP.getAllPrograms();
    }

    @GetMapping("/{id}")
    public Program_OP getProgramById(@PathVariable Long id) {
        return programServiceOP.getProgramById(id);
    }

    @PostMapping
    public Program_OP createProgram(@RequestBody Program_OP programOP) {
        return programServiceOP.createProgram(programOP);
    }

    @PutMapping("/{id}")
    public Program_OP updateProgram(@PathVariable Long id, @RequestBody Program_OP programOP) {
        return programServiceOP.updateProgram(id, programOP);
    }

    @DeleteMapping("/{id}")
    public String deleteProgram(@PathVariable Long id) {
        programServiceOP.deleteProgram(id);
        return "Program with ID " + id + " deleted.";
    }
}
