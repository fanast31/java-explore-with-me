package ru.practicum.main.service.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String email;

}
