package com.warehouse.universitywarehouse.controller.OP;

import com.warehouse.universitywarehouse.model.OP.Faculty_OP;
import com.warehouse.universitywarehouse.service.OP.FacultyService_OP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculties")
public class FacultyController_OP {

    @Autowired
    private FacultyService_OP facultyServiceOP;

    @GetMapping
    public List<Faculty_OP> getAllFaculties() {
        return facultyServiceOP.getAllFaculties();
    }

    @GetMapping("/{id}")
    public Faculty_OP getFacultyById(@PathVariable Long id) {
        return facultyServiceOP.getFacultyById(id);
    }

    @PostMapping
    public Faculty_OP createFaculty(@RequestBody Faculty_OP facultyOP) {
        return facultyServiceOP.createFaculty(facultyOP);
    }

    @PutMapping("/{id}")
    public Faculty_OP updateFaculty(@PathVariable Long id, @RequestBody Faculty_OP facultyOP) {
        return facultyServiceOP.updateFaculty(id, facultyOP);
    }

    @DeleteMapping("/{id}")
    public String deleteFaculty(@PathVariable Long id) {
        facultyServiceOP.deleteFaculty(id);
        return "Faculty with ID " + id + " deleted.";
    }
}
