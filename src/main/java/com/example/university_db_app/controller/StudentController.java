package com.example.university_db_app.controller;

import com.example.university_db_app.model.Student;
import com.example.university_db_app.repository.StudentRepository;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/legacy/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Student not found: " + id));
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        student.setId(null);
        return studentRepository.save(student);
    }

    @PutMapping("/{id}")
    public Student updateStudent(
            @PathVariable Long id,
            @RequestBody Student student) {

        Student existing = studentRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Student not found: " + id));

        existing.setStudentCode(student.getStudentCode());
        existing.setFullName(student.getFullName());
        existing.setEmail(student.getEmail());

        return studentRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found: " + id);
        }

        studentRepository.deleteById(id);
    }
}