
package com.example.university_db_app.controller;

import com.example.university_db_app.model.Enrollment;
import com.example.university_db_app.model.Student;
import com.example.university_db_app.model.Course;

import com.example.university_db_app.repository.EnrollmentRepository;
import com.example.university_db_app.repository.StudentRepository;
import com.example.university_db_app.repository.CourseRepository;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/legacy/enrollments")
public class EnrollmentController {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentController(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Enrollment getEnrollmentById(@PathVariable Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Enrollment not found: " + id));
    }

    @PostMapping
    public Enrollment createEnrollment(@RequestBody Enrollment enrollment) {
        enrollment.setId(null);

        Long studentId = enrollment.getStudent().getId();
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                    new RuntimeException("Student not found: " + studentId));

        Long courseId = enrollment.getCourse().getId();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                    new RuntimeException("Course not found: " + courseId));

        enrollment.setStudent(student);
        enrollment.setCourse(course);

        return enrollmentRepository.save(enrollment);
    }

    @PutMapping("/{id}")
    public Enrollment updateEnrollment(
            @PathVariable Long id,
            @RequestBody Enrollment enrollment) {

        Enrollment existing = enrollmentRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Enrollment not found: " + id));

        Long studentId = enrollment.getStudent().getId();
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                    new RuntimeException("Student not found: " + studentId));

        Long courseId = enrollment.getCourse().getId();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                    new RuntimeException("Course not found: " + courseId));

        existing.setStudent(student);
        existing.setCourse(course);
        existing.setGrade(enrollment.getGrade());

        return enrollmentRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteEnrollment(@PathVariable Long id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new RuntimeException("Enrollment not found: " + id);
        }

        enrollmentRepository.deleteById(id);
    }
}
