package com.pavlov.first_rest.entry;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "student")
@Setter
@Getter
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
    @Column(name = "save_date")
    LocalDateTime saveDate;
    @Column(name = "last_update_time")
    LocalDateTime lastUpdateTime;

    /**
     * метод для сравнения текущего объекта с объектом передакаемом в параметре метода
     *
     * @param o передаваемый для сравнения объект
     * @return возвращает true, если данный объект имеет ту же ссылку, что и переданый.
     * Если переданый объект не ссылается на null и классы текущего объекта и переданного не равны, то возвращаем false.
     * В противном случае мы продолжаем проверку на равенство.
     * В конце, мы возвращаем объединенный результат сравнения каждого свойства по отдельности.
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ?
                ((HibernateProxy) o).getHibernateLazyInitializer()
                        .getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ?
                ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Student student = (Student) o;
        return getId() != 0 && Objects.equals(getId(), student.getId());
    }

    /**
     * @return возвращает числовое представление объекта
     */
    @Override
    public int hashCode() {
        return this instanceof HibernateProxy ?
                ((HibernateProxy) this).getHibernateLazyInitializer()
                        .getPersistentClass().hashCode() : getClass().hashCode();
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
