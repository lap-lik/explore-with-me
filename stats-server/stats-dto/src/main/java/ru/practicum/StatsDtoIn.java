package ru.practicum;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class StatsDtoIn {

    @NotBlank(message = "The app field cannot be empty.")
    private String app;

    @NotBlank(message = "The uri field cannot be empty.")
    private String uri;

    @NotBlank(message = "The ip field cannot be empty.")
    private String ip;

    @NotNull(message = "The timestamp field cannot be null.")
    private String timestamp;
}
