package com.example.university_db_app.controller;

import com.example.university_db_app.model.Lecturer;
import com.example.university_db_app.model.Department;
import com.example.university_db_app.repository.LecturerRepository;
import com.example.university_db_app.repository.DepartmentRepository;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/legacy/lecturers")
public class LecturerController {

    private final LecturerRepository lecturerRepository;
    private final DepartmentRepository departmentRepository;

    public LecturerController(
            LecturerRepository lecturerRepository,
            DepartmentRepository departmentRepository) {
        this.lecturerRepository = lecturerRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping
    public List<Lecturer> getAllLecturers() {
        return lecturerRepository.findAll();
    }

    @GetMapping("/{id}")
    public Lecturer getLecturerById(@PathVariable Long id) {
        return lecturerRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Lecturer not found: " + id));
    }

    @PostMapping
    public Lecturer createLecturer(@RequestBody Lecturer lecturer) {
        lecturer.setId(null);

        Long departmentId = lecturer.getDepartment().getId();

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() ->
                    new RuntimeException("Department not found: " + departmentId));

        lecturer.setDepartment(department);

        return lecturerRepository.save(lecturer);
    }

    @PutMapping("/{id}")
    public Lecturer updateLecturer(
            @PathVariable Long id,
            @RequestBody Lecturer lecturer) {

        Lecturer existing = lecturerRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Lecturer not found: " + id));

        existing.setName(lecturer.getName());
        existing.setEmail(lecturer.getEmail());

        Long departmentId = lecturer.getDepartment().getId();

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() ->
                    new RuntimeException("Department not found: " + departmentId));

        existing.setDepartment(department);

        return lecturerRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteLecturer(@PathVariable Long id) {
        if (!lecturerRepository.existsById(id)) {
            throw new RuntimeException("Lecturer not found: " + id);
        }

        lecturerRepository.deleteById(id);
    }
}