package com.pavlov.first_rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavlov.first_rest.AbstractTest;
import com.pavlov.first_rest.dto.StudentDto;
import com.pavlov.first_rest.entry.Student;
import com.pavlov.first_rest.repository.StudentRepo;
import com.pavlov.first_rest.service.StudentService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link MainController}
 */
@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest extends AbstractTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StudentRepo repository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Добавление новой сущности student в базу")
    public void testAddStudent() throws Exception {
        int id = 1;
        StudentDto studentDto = new StudentDto("Alex", 20);
        String studentDtoJson = objectMapper.writeValueAsString(studentDto);
        var resp = mockMvc.perform(post("/students")
                        .content(studentDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Student expectedStudent = repository.findById(id).get();
        assertAll(
                () -> assertEquals("Alex", expectedStudent.getName()),
                () -> assertEquals(20, expectedStudent.getAge()),
                ()-> repository.deleteById(id),
                () -> assertEquals("/studentsa/" + expectedStudent.getId(),
                        resp.andReturn().getResponse().getHeader("Location"))
        );
    }

    @Test
    @DisplayName("Возвращение всех записей student из базы")
    public void testGetAllStudents() throws Exception {
        createTestStudent("Alex", 20);
        createTestStudent("Tom", 28);
        var response = mockMvc.perform(
                        get("/students")
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        assertEquals("[{\"id\":1,\"name\":\"Alex\",\"age\":20},{\"id\":2,\"name\":\"Tom\",\"age\":28}]",
                response.getResponse().getContentAsString());
    }

    @Test
    public void testFindStudentById() throws Exception {
        int id = createTestStudent("Alex", 19).getId();
        mockMvc.perform(get("/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alex"))
                .andExpect(jsonPath("$.age").value(19));
    }

    @Test
    public void testDeleteStudentById() throws Exception {
        long id = createTestStudent("Alex", 19).getId();
        mockMvc.perform(delete("/students/{id}", id))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Изменение студента с определённым id по имени/возрасту")
    public void testUpdateStudentById() throws Exception {
        int id = createTestStudent("Alex",19).getId();
        StudentDto studentDto = StudentDto.builder().name("Oleg").build();
        String studentDtoJson = objectMapper.writeValueAsString(studentDto);
        mockMvc.perform(patch("/students/{id}", id)
                        .content(studentDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Student actualdStudent = repository.findById(id).get();
        assertAll(
                () -> assertEquals(1, actualdStudent.getId()),
                () -> assertEquals("Oleg", actualdStudent.getName()),
                () -> assertEquals(19, actualdStudent.getAge())
        );
        StudentDto studentDto2 = StudentDto.builder().age(25).build();
        String studentDtoJson2 = objectMapper.writeValueAsString(studentDto2);
        mockMvc.perform(patch("/students/{id}", id)
                        .content(studentDtoJson2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Student actualStudent2 = repository.findById(id).get();
        assertAll(
                () -> assertEquals(1, actualStudent2.getId()),
                () -> assertEquals("Oleg", actualStudent2.getName()),
                () -> assertEquals(25, actualStudent2.getAge())
        );
    }

    private Student createTestStudent(String name, int age) {
        Student person = Student.builder().setName(name).setAge(age).build();
        return repository.save(person);
    }
    @AfterEach
    public void tearDown() {
        //Очистка таблицы student от данных
        jdbcTemplate.execute("TRUNCATE TABLE student");
        //Обнуление счетчика столбца id
        jdbcTemplate.execute("ALTER TABLE student ALTER COLUMN id RESTART WITH 1");
    }
}
