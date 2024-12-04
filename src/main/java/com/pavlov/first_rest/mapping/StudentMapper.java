package com.pavlov.first_rest.mapping;

import com.pavlov.first_rest.dto.StudentDto;
import com.pavlov.first_rest.entry.Student;
import org.mapstruct.Mapper;

@Mapper
public interface StudentMapper {
    StudentDto toStudentDto(Student student);
    Student toStudent(StudentDto studentDto);
}
