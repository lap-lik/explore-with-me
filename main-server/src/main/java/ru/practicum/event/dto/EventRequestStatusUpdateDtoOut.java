package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.request.dto.ParticipationDtoOut;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateDtoOut {

    private List<ParticipationDtoOut> confirmedRequests;

    private List<ParticipationDtoOut> rejectedRequests;
}
