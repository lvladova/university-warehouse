package com.warehouse.universitywarehouse.repository.DW;

import com.warehouse.universitywarehouse.model.DW.Course_DW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository_DW extends JpaRepository<Course_DW, Long> {
}