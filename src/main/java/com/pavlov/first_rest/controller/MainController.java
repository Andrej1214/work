package com.pavlov.first_rest.controller;

import com.pavlov.first_rest.dto.StudentDto;
import com.pavlov.first_rest.entry.Student;
import com.pavlov.first_rest.mapping.StudentMapper;
import com.pavlov.first_rest.service.impl.StudentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

/**
 * RestController для CRUD-операций с сущностью Student
 */
@Tag(name = "Student")
@Slf4j
@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class MainController {
    private final StudentServiceImpl studentServiceImp;
    private final StudentMapper studentMapper;

    /**
     * пердназначен для записи данных передаваемой сущности в базу
     * @param studentDto данные сущности Student передаваемые в таблицу student
     * @return статус 201
     */
    @Operation(
            summary = "добавление студента",
            description = "сущность собирается из передаваемого клиентом studentDto и затем ложится в базу"
    )
    @PostMapping()
    public ResponseEntity<?> addStudent(@RequestBody StudentDto studentDto) {
        Student student = studentServiceImp.saveStudent(
                studentMapper.toStudent(studentDto)
        );
        student.setSaveDate(LocalDateTime.now());
        log.info("Add one new user: {}", student);
        // при добавлении сущности в заголовок добавлять Location с url созданной сущности
        return ResponseEntity.created(URI.create("/students/" + student.getId())).build();
    }

    /**
     * @return список всех студентов
     */
    @Operation(
            summary = "возвращает список всех студентов"
    )
    @GetMapping()
    public List<Student> getAllStudents() {
        return studentServiceImp.getStudents();
    }

    /**
     * @param id искомого студента
     * @return данные сущности Student с указанным id
     */
    @Operation(
            summary = "поиск студента по id"
    )
    @GetMapping("/{id}")
    public StudentDto findStudentById(@PathVariable int id) {
        Student serchedStudent = studentServiceImp.getStudent(id);
        return new StudentDto(serchedStudent.getName(), serchedStudent.getAge());
    }

    /**
     * @param id искомого студента
     * @return статус 204
     */
    @Operation(summary = "удаление студента по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudentById(@PathVariable int id) {
        studentServiceImp.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * @param id искомого студента
     * @param studentDto сущность содержашая обновляемые данные
     * @return обновленную сущность
     */
    @Operation(summary = "обновление полей студента, определяемого по id",
    description = "для осуществления обновления в studentDto могут передаваться как все поля, так и только те, что необоходимо обновить")
    @PatchMapping("/{id}")
    public StudentDto updateStudentById(@PathVariable int id, @RequestBody StudentDto studentDto) {
        Student updatedStudent = studentServiceImp.updateStudent(id, studentDto);
        return new StudentDto(updatedStudent.getName(), updatedStudent.getAge());
    }
    //При создании ресурса добавлять в Location (Header) путь к созданному ресурсу. POST - 201 - Location
    //PUT - ПОЛНОЕ ОБНОВЛЕНИЕ, PATCH - НЕКОТОРЫЕ ПОЛЯ
    //Try to be more precise.
    //If the resource existed once but now has been deleted or deactivated, use 410 Gone.
    //If the resource is available, but the user is not allowed to view it, return a 403 Forbidden.
    //Keep Business Logic on the Server-Side
    //указание версии API
    //Read: Use GET for reading resources.
    //Create: Use POST or PUT for creating new resources.
    //Update: Use PUT and PATCH for updating existing resources.
    //Delete: Use DELETE for deleting existing resources.
    //GET(Idempotent),PUT(Idempotent),PATCH(Idempotent),DELETE(Idempotent),        POST(Not Idempotent)
    //Use CamelCase for Attribute Names  { "yearOfBirth": 1982 }
    //Use Verbs for Operations   GET /translate?from=de_DE&to=en_US&text=Hallo
    //Provide Pagination  Offset-based Pagination    /employees?offset=30&limit=15 # returns the employees 30 to 45
    //author: Philipp Hauer - Head Of Engineering at commercetools
}
