package com.example.university_db_app.controller;

import com.example.university_db_app.model.Department;
import com.example.university_db_app.repository.DepartmentRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/legacy/departments")
public class DepartmentController {

    private final DepartmentRepository repository;

    public DepartmentController(DepartmentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Department> getAllDepartments() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Department not found: " + id));
    }

    @PostMapping
    public Department createDepartment(@RequestBody Department department) {
        department.setId(null);
        return repository.save(department);
    }

    @PutMapping("/{id}")
    public Department updateDepartment(
            @PathVariable Long id,
            @RequestBody Department department) {

        Department existing = repository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Department not found: " + id));

        existing.setCode(department.getCode());
        existing.setName(department.getName());

        return repository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Department not found: " + id);
        }

        repository.deleteById(id);
    }
}