
package com.example.university_db_app.dto;

public record CourseCountDto(
        String courseCode,
        String courseTitle,
        Long studentCount
) {
    public static CourseCountDto from(Object[] row) {
        return new CourseCountDto(
                (String) row[1],
                (String) row[2],
                ((Number) row[3]).longValue()
        );
    }
}
