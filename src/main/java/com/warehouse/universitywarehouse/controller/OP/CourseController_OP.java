package com.warehouse.universitywarehouse.controller.OP;

import com.warehouse.universitywarehouse.model.OP.Course_OP;
import com.warehouse.universitywarehouse.service.OP.CourseService_OP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController_OP {

    @Autowired
    private CourseService_OP courseServiceOP;

    @GetMapping
    public List<Course_OP> getAllCourses() {
        return courseServiceOP.getAllCourses();
    }

    @GetMapping("/{id}")
    public Course_OP getCourseById(@PathVariable Long id) {
        return courseServiceOP.getCourseById(id);
    }

    @PostMapping
    public Course_OP createCourse(@RequestBody Course_OP courseOP) {
        return courseServiceOP.createCourse(courseOP);
    }

    @PutMapping("/{id}")
    public Course_OP updateCourse(@PathVariable Long id, @RequestBody Course_OP courseOP) {
        return courseServiceOP.updateCourse(id, courseOP);
    }

    @DeleteMapping("/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseServiceOP.deleteCourse(id);
        return "Course with ID " + id + " deleted.";
    }
}
