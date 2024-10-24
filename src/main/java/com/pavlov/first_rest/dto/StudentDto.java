package com.pavlov.first_rest.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

/**
 * DTO for {@link com.pavlov.first_rest.entry.Student}
 */
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE)
@Getter
@Setter
public class StudentDto {
    String name;
    int age;

    /**
     * метод для сравнения текущего объекта с объектом передакаемом в параметре метода
     * @param o передаваемый для сравнения объект
     * @return возвращает true, если данный объект имеет ту же ссылку, что и переданый.
     * Если переданый объект не ссылается на null и классы текущего объекта и переданного не равны, то возвращаем false.
     * В противном случае мы продолжаем проверку на равенство.
     * В конце, мы возвращаем объединенный результат сравнения каждого свойства по отдельности.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentDto that = (StudentDto) o;
        return age == that.age && Objects.equals(name, that.name);
    }

    /**
     * @return возвращает числовое представление объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    /**
     * @return возвращает строковое представление обекта
     */
    @Override
    public String toString() {
        return "StudentDto{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
