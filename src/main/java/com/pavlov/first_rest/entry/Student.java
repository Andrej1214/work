package com.pavlov.first_rest.entry;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Entity
@Table(name="student")
@Setter
@Getter()
@Builder(setterPrefix = "set")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "name")
    String name;
    @Column(name = "age")
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
        Student student = (Student) o;
        return id == student.id && age == student.age && Objects.equals(name, student.name);
    }

    /**
     * @return возвращает числовое представление объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, age);
    }

    /**
     * @return возвращает строковое представление обекта
     */
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
