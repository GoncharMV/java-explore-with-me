package ru.practicum.participation_request.dto;

import lombok.*;
import ru.practicum.utils.enums.RequestStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequestUpdateDto {

    List<Long> requestIds;
    RequestStatus status;

}
