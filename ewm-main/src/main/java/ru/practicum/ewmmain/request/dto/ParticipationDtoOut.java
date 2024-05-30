package ru.practicum.ewmmain.request.dto;

import lombok.Data;

@Data
public class ParticipationDtoOut {

    private Long id;

    private String created;

    private Long event;

    private Long requester;

    private String status;
}
