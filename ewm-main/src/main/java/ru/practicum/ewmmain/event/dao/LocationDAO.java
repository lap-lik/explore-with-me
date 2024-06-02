package ru.practicum.ewmmain.event.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmain.event.model.Location;

public interface LocationDAO extends JpaRepository<Location, Long> {

    Location findByLatitudeAndLongitude(Double latitude, Double longitude);
}
