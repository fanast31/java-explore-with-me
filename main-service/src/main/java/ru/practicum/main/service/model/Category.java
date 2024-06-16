package ru.practicum.main.service.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "categories")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 50)
    private String name;
}
