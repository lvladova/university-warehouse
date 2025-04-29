package com.warehouse.universitywarehouse.service.OP;

import com.warehouse.universitywarehouse.model.OP.Student_OP;
import com.warehouse.universitywarehouse.repository.OP.StudentRepository_OP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService_OP {

    @Autowired
    private StudentRepository_OP studentRepositoryOP;

    public List<Student_OP> getAllStudents() {
        return studentRepositoryOP.findAll();
    }

    public Student_OP getStudentById(Long id) {
        return studentRepositoryOP.findById(id).orElse(null);
    }

    public Student_OP createStudent(Student_OP studentOP) {
        return studentRepositoryOP.save(studentOP);
    }

    public Student_OP updateStudent(Long id, Student_OP updatedStudentOP) {
        Student_OP existingStudentOP = studentRepositoryOP.findById(id).orElse(null);
        if (existingStudentOP != null) {
            existingStudentOP.setName(updatedStudentOP.getName());
            existingStudentOP.setAge(updatedStudentOP.getAge());
            existingStudentOP.setGender(updatedStudentOP.getGender());
            existingStudentOP.setNationality(updatedStudentOP.getNationality());
            return studentRepositoryOP.save(existingStudentOP);
        }
        return null;
    }

    public void deleteStudent(Long id) {
        studentRepositoryOP.deleteById(id);
    }
}

