package ru.practicum.participation_request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.utils.ConstantUtil;
import ru.practicum.utils.enums.RequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDto {

    private Long id;

    @JsonFormat(pattern = ConstantUtil.DATA_FORMAT)
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private RequestStatus status;

}
