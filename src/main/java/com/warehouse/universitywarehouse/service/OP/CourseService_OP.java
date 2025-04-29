package com.warehouse.universitywarehouse.service.OP;

import com.warehouse.universitywarehouse.model.OP.Course_OP;
import com.warehouse.universitywarehouse.repository.OP.CourseRepository_OP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService_OP {

    @Autowired
    private CourseRepository_OP courseRepositoryOP;

    public List<Course_OP> getAllCourses() {
        return courseRepositoryOP.findAll();
    }

    public Course_OP getCourseById(Long id) {
        return courseRepositoryOP.findById(id).orElse(null);
    }

    public Course_OP createCourse(Course_OP courseOP) {
        return courseRepositoryOP.save(courseOP);
    }

    public Course_OP updateCourse(Long id, Course_OP updatedCourseOP) {
        Course_OP existingCourseOP = courseRepositoryOP.findById(id).orElse(null);
        if (existingCourseOP != null) {
            existingCourseOP.setCourseName(updatedCourseOP.getCourseName());
            existingCourseOP.setDepartmentId(updatedCourseOP.getDepartmentId());
            return courseRepositoryOP.save(existingCourseOP);
        }
        return null;
    }

    public void deleteCourse(Long id) {
        courseRepositoryOP.deleteById(id);
    }
}
