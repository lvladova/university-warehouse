package com.warehouse.universitywarehouse.service.OP;

import com.warehouse.universitywarehouse.model.OP.Faculty_OP;
import com.warehouse.universitywarehouse.repository.OP.FacultyRepository_OP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService_OP {

    @Autowired
    private FacultyRepository_OP facultyRepositoryOP;

    public List<Faculty_OP> getAllFaculties() {
        return facultyRepositoryOP.findAll();
    }

    public Faculty_OP getFacultyById(Long id) {
        return facultyRepositoryOP.findById(id).orElse(null);
    }

    public Faculty_OP createFaculty(Faculty_OP facultyOP) {
        return facultyRepositoryOP.save(facultyOP);
    }

    public Faculty_OP updateFaculty(Long id, Faculty_OP updatedFacultyOP) {
        Faculty_OP facultyOP = facultyRepositoryOP.findById(id).orElse(null);
        if (facultyOP != null) {
            facultyOP.setFacultyName(updatedFacultyOP.getFacultyName());
            return facultyRepositoryOP.save(facultyOP);
        }
        return null;
    }

    public void deleteFaculty(Long id) {
        facultyRepositoryOP.deleteById(id);
    }
}
