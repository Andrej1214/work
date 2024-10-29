package com.pavlov.first_rest.controller;

import com.pavlov.first_rest.entry.Student;
import com.pavlov.first_rest.repository.StudentRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the {@link MainController}
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Tag("integration")
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StudentRepo repository;

    private final int id = 1;


    @Test
    @DisplayName("Проверка добавления новой сущности student в базу")
    public void addStudent() throws Exception {
        String studentDto = """
                {
                    "name": "Alex",
                    "age": 20
                }""";

        mockMvc.perform(post("/students")
                        .content(studentDto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Student expectedStudent = repository.findById(id).get();
        assertAll(
                ()->{ assertEquals("Alex", expectedStudent.getName()); },
                ()->{ assertEquals(20, expectedStudent.getAge()); }

        );
    }

    @Test
    public void getAllStudents() throws Exception {
        addStudent();
        addStudent();
        var response = mockMvc.perform(
                        get("/students")
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals("[{\"id\":1,\"name\":\"Alex\",\"age\":20},{\"id\":2,\"name\":\"Alex\",\"age\":20}]",
                response.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("")
    public void findStudentById() throws Exception {
        long id = createTestStudent("Alex",19).getId();

        mockMvc.perform(get("/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alex"))
                .andExpect(jsonPath("$.age").value(19));
    }

    @Test
    public void deleteStudentById() throws Exception {
        long id = createTestStudent("Alex",19).getId();
        mockMvc.perform(delete("/students/{id}", id))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void updateStudentById() throws Exception {
        long id = createTestStudent("Alex",19).getId();
        String studentDto = """
                {
                    "name": "Oleg",
                    "age": 25
                }""";

        mockMvc.perform(patch("/students/{0}", "0")
                        .content(studentDto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
    private Student createTestStudent(String name,int age) {
        Student person = new Student(id,name,age);
        return repository.save(person);
    }
}
