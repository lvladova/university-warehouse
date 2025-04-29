package com.warehouse.universitywarehouse.controller.OP;

import com.warehouse.universitywarehouse.model.OP.Module_OP;
import com.warehouse.universitywarehouse.service.OP.ModuleService_OP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
public class ModuleController_OP {

    @Autowired
    private ModuleService_OP moduleServiceOP;

    @GetMapping
    public List<Module_OP> getAllModules() {
        return moduleServiceOP.getAllModules();
    }

    @GetMapping("/{id}")
    public Module_OP getModuleById(@PathVariable Long id) {
        return moduleServiceOP.getModuleById(id);
    }

    @PostMapping
    public Module_OP createModule(@RequestBody Module_OP moduleOP) {
        return moduleServiceOP.createModule(moduleOP);
    }

    @PutMapping("/{id}")
    public Module_OP updateModule(@PathVariable Long id, @RequestBody Module_OP moduleOP) {
        return moduleServiceOP.updateModule(id, moduleOP);
    }

    @DeleteMapping("/{id}")
    public String deleteModule(@PathVariable Long id) {
        moduleServiceOP.deleteModule(id);
        return "Module with ID " + id + " deleted.";
    }
}
