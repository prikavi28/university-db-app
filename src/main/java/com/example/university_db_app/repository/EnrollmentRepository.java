
package com.example.university_db_app.repository;

import com.example.university_db_app.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnrollmentRepository
        extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudent_StudentCode(String studentCode);

    List<Enrollment> findByCourse_Code(String courseCode);

    List<Enrollment> findByGrade(String grade);

    @Query("""
        SELECT c.id, c.code, c.title, COUNT(e.id)
        FROM Course c
        LEFT JOIN c.enrollments e
        GROUP BY c.id, c.code, c.title
        ORDER BY c.code
        """)
    List<Object[]> countStudentsPerCourse();
}
