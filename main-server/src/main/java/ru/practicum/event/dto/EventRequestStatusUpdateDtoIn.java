package ru.practicum.event.dto;

import lombok.Data;
import ru.practicum.request.model.RequestStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EventRequestStatusUpdateDtoIn {

    @NotNull(message = "The list of requestIds field cannot be null.")
    @NotEmpty(message = "The list of requestIds field cannot be null.")
    private List<Long> requestIds;

    @NotNull(message = "The status field cannot be null.")
    private RequestStatus status;
}
