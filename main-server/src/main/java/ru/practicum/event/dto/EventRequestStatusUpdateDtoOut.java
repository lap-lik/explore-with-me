package ru.practicum.event.dto;

import lombok.Data;
import ru.practicum.request.dto.ParticipationDtoOut;

import java.util.List;
@Data
public class EventRequestStatusUpdateDtoOut {

    private List<ParticipationDtoOut> confirmedRequests;

    private List<ParticipationDtoOut> rejectedRequests;
}
