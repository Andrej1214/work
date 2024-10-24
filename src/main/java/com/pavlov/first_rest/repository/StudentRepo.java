package com.pavlov.first_rest.repository;

import com.pavlov.first_rest.entry.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * интерфейс для работы с JpaRepository по сущности Student
 */
@Repository
public interface StudentRepo extends JpaRepository<Student, Integer> {
}
