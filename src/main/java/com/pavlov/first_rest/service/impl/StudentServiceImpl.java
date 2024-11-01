package com.pavlov.first_rest.service.impl;

import com.pavlov.first_rest.dto.StudentDto;
import com.pavlov.first_rest.entry.Student;
import com.pavlov.first_rest.exception.CustomException;
import com.pavlov.first_rest.repository.StudentRepo;
import com.pavlov.first_rest.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Реализация интерфейса StudentService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepo studentRepo;

    /**
     * @return возвращает список студентов отсортированных по имени
     */
    @Override
    public List<Student> getStudents() {
        if (studentRepo.findAll().isEmpty()) {
            throw new CustomException("List of students is empty");
        }
        return studentRepo.findAll().stream()
                .sorted(Comparator.comparing(Student::getName))
                .toList();
    }

    /**
     * @param id студента
     * @return возвращает студента из репозитория по указанному id
     */
    @Override
    public Student getStudent(int id) {
        return studentRepo.findById(id).stream().findFirst()
                .orElseThrow(() -> new CustomException("Student with id=" + id + " not found"));
    }

    /**
     * @param student сохраняемая сущность
     * @return возвращается сущность сохраненная в базе
     */
    @Override
    public Student saveStudent(Student student) {
        return studentRepo.save(student);
    }

    /**
     * @param id         студента, которого необходимо обновить
     * @param studentDto DTO сущности студент, для обновления значений таблицы student по указанному id
     * @return возвращает сущность с обновленныыми данными
     */
    public Student updateStudent(int id, StudentDto studentDto) {
        Student student = studentRepo.findById(id).stream().findFirst()
                .orElseThrow(() -> new CustomException("Student with id=" + id + " not found"));
        String name = studentDto.getName() == null||studentDto.getName().isEmpty() ?
                student.getName() : studentDto.getName();
        int age = studentDto.getAge() == 0 ? student.getAge() : studentDto.getAge();
        Student updatedStudent = Student.builder()
                .setId(id)
                .setName(name)
                .setAge(age)
                .build();
        return studentRepo.save(updatedStudent);
    }

    /**
     * @param id студента которого хотиим удалить
     */
    @Override
    public void deleteStudent(int id) {
        if (!studentRepo.existsById(id)) {
            throw new CustomException("Student with id=" + id + " not found");
        }
        studentRepo.deleteById(id);
    }
}
