package com.warehouse.universitywarehouse.service.OP;

import com.warehouse.universitywarehouse.model.OP.Module_OP;
import com.warehouse.universitywarehouse.repository.OP.ModuleRepository_OP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService_OP {

    @Autowired
    private ModuleRepository_OP moduleRepositoryOP;

    public List<Module_OP> getAllModules() {
        return moduleRepositoryOP.findAll();
    }

    public Module_OP getModuleById(Long id) {
        return moduleRepositoryOP.findById(id).orElse(null);
    }

    public Module_OP createModule(Module_OP moduleOP) {
        return moduleRepositoryOP.save(moduleOP);
    }

    public Module_OP updateModule(Long id, Module_OP updatedModuleOP) {
        Module_OP existingModuleOP = moduleRepositoryOP.findById(id).orElse(null);
        if (existingModuleOP != null) {
            existingModuleOP.setModuleName(updatedModuleOP.getModuleName());
            existingModuleOP.setCourseId(updatedModuleOP.getCourseId());
            return moduleRepositoryOP.save(existingModuleOP);
        }
        return null;
    }

    public void deleteModule(Long id) {
        moduleRepositoryOP.deleteById(id);
    }
}
