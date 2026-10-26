
package com.example.university_db_app.dto;

import com.example.university_db_app.model.Student;

public record StudentDto(
        String studentCode,
        String fullName,
        String email
) {
    public static StudentDto from(Student student) {
        return new StudentDto(
                student.getStudentCode(),
                student.getFullName(),
                student.getEmail()
        );
    }
}
