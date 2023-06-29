package ru.practicum.participation_request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateStatusResultDto {

    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;

}
