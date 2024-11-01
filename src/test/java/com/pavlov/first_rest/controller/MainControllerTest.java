package com.pavlov.first_rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.pavlov.first_rest.AbstractTest;
import com.pavlov.first_rest.AbstractTest;
import com.pavlov.first_rest.dto.StudentDto;
import com.pavlov.first_rest.entry.Student;
import com.pavlov.first_rest.repository.StudentRepo;
import com.pavlov.first_rest.service.StudentService;
import org.h2.engine.DbObject;
import org.h2.schema.Sequence;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@TestInstance(TestInstance.Lifecycle.PER_METHOD)
//@Sql(scripts = "/db.script/delete_all_from_student.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MainControllerTest extends AbstractTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StudentRepo repository;
    @Autowired
    private StudentService studentService;

    // вопрос с обнулением инкремента при запуске тестов через класс (база h2 или postgres)

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Order(1)
    @Test
    @DisplayName("Проверка добавления новой сущности student в базу")
//    @Sql(scripts = "/db.script/delete_all_from_student.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testAddStudent() throws Exception {
        int id = 1;
        StudentDto studentDto = new StudentDto("Alex", 20);
        String studentDtoJson = objectMapper.writeValueAsString(studentDto);
        mockMvc.perform(post("/students")
                        .content(studentDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Student expectedStudent = repository.findById(id).get();
        assertAll(
                () -> assertEquals("Alex", expectedStudent.getName()),
                () -> assertEquals(20, expectedStudent.getAge()),
                ()-> repository.deleteById(id)
        );
    }

    @Order(5)
    @Test
    @DisplayName("Проверка возвращения из базы всех записей student")
//    @Sql(scripts = "/db.script/delete_all_from_student.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetAllStudents() throws Exception {
        createTestStudent("Alex", 20);
        createTestStudent("Alex", 20);
        var response = mockMvc.perform(
                        get("/students")
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();
        System.out.println("Все студенты");
        studentService.getStudents().forEach(System.out::println);

        assertEquals("[{\"id\":1,\"name\":\"Alex\",\"age\":20},{\"id\":2,\"name\":\"Alex\",\"age\":20}]",
//        assertEquals("[{\"id\":5,\"name\":\"Alex\",\"age\":20},{\"id\":6,\"name\":\"Alex\",\"age\":20}]",
                response.getResponse().getContentAsString());
    }

    @Order(2)
    @Test
    @DisplayName("")
    public void findStudentById() throws Exception {
        int id = createTestStudent("Alex", 19).getId();
        mockMvc.perform(get("/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alex"))
                .andExpect(jsonPath("$.age").value(19));
    }

    @Order(4)
    @Test
    public void deleteStudentById() throws Exception {
        long id = createTestStudent("Alex", 19).getId();
        mockMvc.perform(delete("/students/{id}", id))
                .andExpect(status().is2xxSuccessful());
    }

    @Order(2)
    @Test
    public void updateStudentById() throws Exception {
        int id = createTestStudent("Alex",19).getId();
        System.out.println(id);
        StudentDto studentDto = StudentDto.builder().name("Oleg").build();
        String studentDtoJson = objectMapper.writeValueAsString(studentDto);
        mockMvc.perform(patch("/students/{id}", id)
                        .content(studentDtoJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Student actualdStudent = repository.findById(id).get();
        assertAll(
//                () -> assertEquals(3, actualdStudent.getId()),
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
//                () -> assertEquals(3, actualStudent2.getId()),
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
//        DbObject.INDEX=1;
    }
}
