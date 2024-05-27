package ru.practicum.ewmmain.category.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryOutputDTO {

    private Long id;
    private String name;
}
