package ru.practicum.main.service.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDtoRequest {
    private List<Long> events;
    private boolean pinned = false;
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
}