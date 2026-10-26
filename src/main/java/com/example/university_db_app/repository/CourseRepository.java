package com.example.university_db_app.repository;

import com.example.university_db_app.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("""
        SELECT c FROM Course c
        JOIN FETCH c.lecturer l
        JOIN FETCH l.department d
        """)
    List<Course> findAllWithLecturerAndDepartment();

    @Query("""
        SELECT c FROM Course c
        JOIN FETCH c.lecturer l
        JOIN FETCH l.department d
        WHERE c.code = :code
        """)
    Optional<Course> findByCodeWithLecturerAndDepartment(
            @Param("code") String code
    );
}