package com.warehouse.universitywarehouse.repository.DW;

import com.warehouse.universitywarehouse.model.DW.Department_DW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository_DW extends JpaRepository<Department_DW, Long> {
}
