package ru.practicum.main.service.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.service.category.model.Category;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
}
