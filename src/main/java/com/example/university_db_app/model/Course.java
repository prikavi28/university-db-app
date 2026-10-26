
package com.example.university_db_app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course")
@Getter
@Setter
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private Integer credits;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_id", nullable = false)
    private Lecturer lecturer;

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments = new ArrayList<>();
}
