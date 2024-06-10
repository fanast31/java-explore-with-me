package ru.practicum.main.service.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.main.service.exceptions.BadRequestException;

public class PaginationUtils {

    public static Pageable createPageable(Integer from, Integer size) {

        if (from == null || size == null || from < 0 || size <= 0) {
            throw new BadRequestException("Bad pagination data");
        }
        int pageNumber = from / size;
        return PageRequest.of(pageNumber, size);
    }

    public static Pageable createPageable(Integer from, Integer size, Sort sort) {

        if (from == null || size == null || from < 0 || size <= 0) {
            throw new BadRequestException("Bad pagination data");
        }
        int pageNumber = from / size;
        return PageRequest.of(pageNumber, size, sort);
    }
}
