package ru.practicum.ewmmain.event.dto;

import lombok.Data;
import ru.practicum.ewmmain.request.dto.ParticipationDtoOut;

import java.util.List;
@Data
public class EventRequestStatusUpdateDtoOut {

    private List<ParticipationDtoOut> confirmedRequests;

    private List<ParticipationDtoOut> rejectedRequests;
}
