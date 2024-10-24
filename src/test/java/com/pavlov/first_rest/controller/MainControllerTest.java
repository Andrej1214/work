package com.pavlov.first_rest.controller;

import com.pavlov.first_rest.dto.StudentDto;
import com.pavlov.first_rest.entry.Student;
import com.pavlov.first_rest.exception.CustomException;
import com.pavlov.first_rest.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MainControllerTest {
    @Mock
    private StudentServiceImpl service;
    @InjectMocks
    private MainController controller;
    private final int id = 1;

    @Test
    public void testUpdateStudentById() {
        StudentDto dto = StudentDto.builder().name("Igor").age(18).build();
        Student student = new Student(1,"Igor",18);
        when(service.updateStudent(id,dto))
                .thenReturn(student);

        assertEquals(dto,controller.updateStudentById(id,dto));
    }
    @Test
    public void testThrowExceptionByUpdateStudentById() {
        StudentDto dto = new StudentDto();
        when(service.updateStudent(id,dto))
                .thenThrow(new CustomException("Student with id=" + id + " not found"));

        var exception = assertThrows(CustomException.class,()->controller.updateStudentById(id,dto));
        assertEquals(exception.getMessage(),"Student with id=" + id + " not found");
    }
}
