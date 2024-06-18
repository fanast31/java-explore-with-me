package ru.practicum.main.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.service.model.request.RequestStatus;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateStatusDto {
    private List<Long> requestIds;
    private RequestStatus status;
}
