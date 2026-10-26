package com.example.university_db_app.dto;

import com.example.university_db_app.model.Course;

public record CourseDto(
        String code,
        String title,
        Integer credits,
        String lecturer,
        String department
) {
    public static CourseDto from(Course course) {
        return new CourseDto(
                course.getCode(),
                course.getTitle(),
                course.getCredits(),
                course.getLecturer().getName(),
                course.getLecturer().getDepartment().getName()
        );
    }
}