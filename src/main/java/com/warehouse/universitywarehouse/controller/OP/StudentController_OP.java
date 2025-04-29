package com.warehouse.universitywarehouse.controller.OP;

import com.warehouse.universitywarehouse.model.OP.Student_OP;
import com.warehouse.universitywarehouse.service.OP.StudentService_OP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController_OP {

    @Autowired
    private StudentService_OP studentServiceOP;

    @GetMapping
    public List<Student_OP> getAllStudents() {
        return studentServiceOP.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student_OP getStudentById(@PathVariable Long id) {
        return studentServiceOP.getStudentById(id);
    }

    @PostMapping
    public Student_OP createStudent(@RequestBody Student_OP studentOP) {
        return studentServiceOP.createStudent(studentOP);
    }

    @PutMapping("/{id}")
    public Student_OP updateStudent(@PathVariable Long id, @RequestBody Student_OP studentOP) {
        return studentServiceOP.updateStudent(id, studentOP);
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentServiceOP.deleteStudent(id);
        return "Student with ID " + id + " deleted.";
    }
}

