package com.example.university_db_app.controller;

import com.example.university_db_app.model.Course;
import com.example.university_db_app.model.Lecturer;
import com.example.university_db_app.repository.CourseRepository;
import com.example.university_db_app.repository.LecturerRepository;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/legacy/courses")
public class CourseController {

    private final CourseRepository courseRepository;
    private final LecturerRepository lecturerRepository;

    public CourseController(
            CourseRepository courseRepository,
            LecturerRepository lecturerRepository) {
        this.courseRepository = courseRepository;
        this.lecturerRepository = lecturerRepository;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Course not found: " + id));
    }

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        course.setId(null);

        Long lecturerId = course.getLecturer().getId();

        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() ->
                    new RuntimeException("Lecturer not found: " + lecturerId));

        course.setLecturer(lecturer);

        return courseRepository.save(course);
    }

    @PutMapping("/{id}")
    public Course updateCourse(
            @PathVariable Long id,
            @RequestBody Course course) {

        Course existing = courseRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Course not found: " + id));

        existing.setCode(course.getCode());
        existing.setTitle(course.getTitle());
        existing.setCredits(course.getCredits());

        Long lecturerId = course.getLecturer().getId();

        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() ->
                    new RuntimeException("Lecturer not found: " + lecturerId));

        existing.setLecturer(lecturer);

        return courseRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found: " + id);
        }

        courseRepository.deleteById(id);
    }
}