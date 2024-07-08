package ru.practicum.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class EwmPageRequest extends PageRequest {

    public EwmPageRequest(int from, int size, Sort sort) {
        super(from > 0 ? from / size : 0, size, sort);
    }

    public static EwmPageRequest of(int page, int size) {
        return of(page, size, Sort.unsorted());
    }

    public static EwmPageRequest of(int page, int size, Sort sort) {
        return new EwmPageRequest(page, size, sort);
    }
}
