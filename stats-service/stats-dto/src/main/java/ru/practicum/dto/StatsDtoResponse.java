package ru.practicum.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class StatsDtoResponse {
    private String app;
    private String uri;
    private Long hits;
}
