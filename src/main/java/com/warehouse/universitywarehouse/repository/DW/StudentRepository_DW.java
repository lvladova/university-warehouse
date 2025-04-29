package com.warehouse.universitywarehouse.repository.DW;

import com.warehouse.universitywarehouse.model.DW.Student_DW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository_DW extends JpaRepository<Student_DW, Long> {
}