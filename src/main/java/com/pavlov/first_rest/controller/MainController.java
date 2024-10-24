package com.pavlov.first_rest.controller;

import com.pavlov.first_rest.dto.StudentDto;
import com.pavlov.first_rest.entry.Student;
import com.pavlov.first_rest.service.impl.StudentServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RestController для CRUD-операций с сущностью Student
 */
@Slf4j
@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class MainController {
    private final StudentServiceImpl userServiceImp;

    /**
     * пердназначен для записи данных передаваемой сущности в базу
     * @param studentDto данные сущности Student передаваемые в таблицу student
     */
    @PostMapping()
    public void addStudent(@RequestBody StudentDto studentDto) {

        log.info("Add one new user: {}", userServiceImp.saveStudent(
                Student.builder()
                .setName(studentDto.getName())
                .setAge(studentDto.getAge())
                        .build()));
    }

    /**
     * @return список всех студентов
     */
    @GetMapping()
    public List<Student> getAllStudents() {
        return userServiceImp.getStudents();
    }

    /**
     * @param id искомого студента
     * @return данные сущности Student с указанным id
     */
    @GetMapping("/{id}")
    public StudentDto findStudentById(@PathVariable int id) {
        Student serchedStudent = userServiceImp.getStudent(id);
        return new StudentDto(serchedStudent.getName(), serchedStudent.getAge());
    }

    /**
     * @param id искомого студента
     * @return статус 204
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteStudentById(@PathVariable int id) {
        userServiceImp.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * @param id искомого студента
     * @param studentDto сущность содержашая обновляемые данные
     * @return обновленную сущность
     */
    @PatchMapping("/{id}")
    public StudentDto updateStudentById(@PathVariable int id, @RequestBody StudentDto studentDto) {
        Student updatedStudent = userServiceImp.updateStudent(id, studentDto);
        return new StudentDto(updatedStudent.getName(), updatedStudent.getAge());
    }
}
