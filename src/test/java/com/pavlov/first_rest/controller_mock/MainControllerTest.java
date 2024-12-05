package com.pavlov.first_rest.controller_mock;

import com.pavlov.first_rest.controller.MainController;
import com.pavlov.first_rest.dto.StudentDto;
import com.pavlov.first_rest.entry.Student;
import com.pavlov.first_rest.exception.CustomNotFoundException;
import com.pavlov.first_rest.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MainControllerTest {
    @Mock
    private StudentServiceImpl service;
    @InjectMocks
    private MainController controller;

    private final int id = 1;
    private final Student student = new Student(1,"Igor",18,null,null);
    private final StudentDto studentDto = StudentDto.builder().name("Igor").age(18).build();

    @Test
    @DisplayName("Проверка на то, что после добавления студента возвращается статус 201")
    public void testAddStudent() {
        when(service.saveStudent(Student.builder().setName(studentDto.getName()).setAge(studentDto.getAge()).build())).thenReturn(student);
        assertEquals(new ResponseEntity<>(HttpStatus.CREATED),controller.addStudent(studentDto));
    }
    @Test
    @DisplayName("Проверка на то, что метод возвращает список студентов")
    public void testGetAllStudents() {
        List<Student>studentList=new ArrayList<>();
        when(service.getStudents()).thenReturn(studentList);
        assertEquals(studentList,controller.getAllStudents());
    }
    @Test
    @DisplayName("Проверка того, что при поиске студента по id возвращается его studentDto")
    public void testFindStudentById() {
        when(service.getStudent(id)).thenReturn(student);
        assertEquals(studentDto,controller.findStudentById(id));
    }
    @Test
    @DisplayName("Проверка того, что при отсутствии студента с таким id метод выбрасывает исключение с текстом \"Student with id=id_number not found\"")
    public void testThrowExceptionByFindStudentById() {
        when(service.getStudent(id)).thenThrow(new CustomNotFoundException("Student with id=" + id + " not found"));

        var exception = assertThrows(CustomNotFoundException.class,()->controller.findStudentById(id));
        assertEquals(exception.getMessage(),"Student with id=" + id + " not found");
    }

    @Test
    @DisplayName("Проверка того, что при удалении студента возвращается статус 204")
    public void testDeleteStudentById() {
        doNothing().when(service).deleteStudent(id);
        assertEquals(new ResponseEntity<>(HttpStatus.NO_CONTENT).getStatusCode(),controller.deleteStudentById(id).getStatusCode());
    }
    @Test
    @DisplayName("Проверка того, что при отсутствии студента с таким id метод выбрасывает исключение с текстом \"Student with id=id_number not found\"")
    public void testThrowExceptionByDeleteStudentById() {
        doThrow(new CustomNotFoundException("Student with id=" + id + " not found"))
                .when(service).deleteStudent(id);

        var exception = assertThrows(CustomNotFoundException.class,()->controller.deleteStudentById(id));
        assertEquals(exception.getMessage(),"Student with id=" + id + " not found");
    }
    @Test
    @DisplayName("Проверка того, что при обновлении студента с таким id метод возвращает его studentDto")
    public void testUpdateStudentById() {
        when(service.updateStudent(id, studentDto))
                .thenReturn(student);

        assertEquals(studentDto,controller.updateStudentById(id, studentDto));
    }
    @Test
    @DisplayName("Проверка того, что при отсутствии студента с таким id метод выбрасывает исключение с текстом \"Student with id=id_number not found\"")
    public void testThrowExceptionByUpdateStudentById() {
        when(service.updateStudent(id, studentDto))
                .thenThrow(new CustomNotFoundException("Student with id=" + id + " not found"));

        var exception = assertThrows(CustomNotFoundException.class,()->controller.updateStudentById(id, studentDto));
        assertEquals(exception.getMessage(),"Student with id=" + id + " not found");
    }
}
