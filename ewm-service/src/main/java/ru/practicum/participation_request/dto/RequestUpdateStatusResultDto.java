package ru.practicum.participation_request.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestUpdateStatusResultDto {

    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;

}
