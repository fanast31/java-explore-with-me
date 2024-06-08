package ru.practicum.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StatsDtoResponse {
    private String app;
    private String uri;
    private Long hits;
}
