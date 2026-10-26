
package com.example.university_db_app.controller;

import com.example.university_db_app.dto.CourseCountDto;
import com.example.university_db_app.dto.CourseDto;
import com.example.university_db_app.dto.EnrollmentDto;
import com.example.university_db_app.dto.LecturerDto;
import com.example.university_db_app.dto.StudentDto;

import com.example.university_db_app.repository.CourseRepository;
import com.example.university_db_app.repository.DepartmentRepository;
import com.example.university_db_app.repository.EnrollmentRepository;
import com.example.university_db_app.repository.StudentRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UniversityController {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final EnrollmentRepository enrollmentRepository;

    public UniversityController(
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            DepartmentRepository departmentRepository,
            EnrollmentRepository enrollmentRepository) {

        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    // Get all students or search by name
    @GetMapping("/students")
    public List<StudentDto> students(
            @RequestParam(required = false) String name) {

        var list = (name == null || name.isBlank())
                ? studentRepository.findAllByOrderByFullNameAsc()
                : studentRepository
                    .findByFullNameContainingIgnoreCase(name.trim());

        return list.stream()
                .map(StudentDto::from)
                .toList();
    }

    // Get a student by student code
    @GetMapping("/students/{code}")
    public ResponseEntity<StudentDto> student(
            @PathVariable String code) {

        return studentRepository.findByStudentCode(code)
                .map(student ->
                        ResponseEntity.ok(StudentDto.from(student)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Get enrollments for a student
    @GetMapping("/students/{code}/enrollments")
    public List<EnrollmentDto> studentEnrollments(
            @PathVariable String code) {

        return enrollmentRepository
                .findByStudent_StudentCode(code)
                .stream()
                .map(EnrollmentDto::from)
                .toList();
    }

    // Get courses, filter by minimum credits or department
    @GetMapping("/courses")
    public List<CourseDto> courses(
            @RequestParam(required = false) Integer minCredits,
            @RequestParam(required = false) String department) {

        return courseRepository
                .findAllWithLecturerAndDepartment()
                .stream()
                .filter(course ->
                        minCredits == null
                        || course.getCredits() >= minCredits)
                .filter(course ->
                        department == null
                        || department.isBlank()
                        || course.getLecturer()
                                .getDepartment()
                                .getName()
                                .equalsIgnoreCase(department.trim()))
                .map(CourseDto::from)
                .toList();
    }

    // Get students enrolled in a course
    @GetMapping("/courses/{code}/students")
    public List<StudentDto> courseStudents(
            @PathVariable String code) {

        return enrollmentRepository
                .findByCourse_Code(code)
                .stream()
                .map(enrollment ->
                        StudentDto.from(enrollment.getStudent()))
                .toList();
    }

    // Get lecturers in a department
    @GetMapping("/departments/{code}/lecturers")
    public ResponseEntity<List<LecturerDto>> departmentLecturers(
            @PathVariable String code) {

        return departmentRepository.findWithLecturers(code)
                .map(department ->
                        ResponseEntity.ok(
                                department.getLecturers()
                                        .stream()
                                        .map(LecturerDto::from)
                                        .toList()))
                .orElse(ResponseEntity.notFound().build());
    }

    // Get enrollments by grade
    @GetMapping("/enrollments")
    public List<EnrollmentDto> enrollments(
            @RequestParam String grade) {

        return enrollmentRepository.findByGrade(grade)
                .stream()
                .map(EnrollmentDto::from)
                .toList();
    }

    // Count students enrolled in each course
    @GetMapping("/reports/course-counts")
    public List<CourseCountDto> courseCounts() {

        return enrollmentRepository
                .countStudentsPerCourse()
                .stream()
                .map(CourseCountDto::from)
                .toList();
    }
}
