package ru.practicum.main.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestListDto {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}