package ru.practicum.event.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "locations")
@Builder(toBuilder = true)
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;
}
