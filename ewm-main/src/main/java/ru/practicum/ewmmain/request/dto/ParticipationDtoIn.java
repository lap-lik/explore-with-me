package ru.practicum.ewmmain.request.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ParticipationDtoIn {

    private String created;

    private Long event;

    private Long requester;

    private String status;
}
