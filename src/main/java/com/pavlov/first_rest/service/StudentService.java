package com.pavlov.first_rest.service;

import com.pavlov.first_rest.entry.Student;

import java.util.List;

/**
 * интерфейс для CRUD операций с сущностью типа Student
 */
public interface StudentService {
    /**
     * @return список всех имеющихся студентов в базе
     */
    List<Student> getStudents();

    /**
     * @param id студента
     * @return сущность студент по указанному id
     */
    Student getStudent(int id);

    /**
     * @param user сохраняемая сущность
     * @return сущность которая была сохранена
     */
    Student saveStudent(Student user);

    /**
     * @param id студента которого хотиим удалить
     */
    void deleteStudent(int id);
}
