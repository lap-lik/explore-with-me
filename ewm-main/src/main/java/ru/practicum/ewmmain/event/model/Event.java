package ru.practicum.ewmmain.event.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewmmain.category.model.Category;
import ru.practicum.ewmmain.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.practicum.ewmmain.constant.Constant.DATE_TIME_PATTERN;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
@Builder(toBuilder = true)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String annotation;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;

    @Column(name = "confirmed_requests")
    private Long confirmedRequests;

    @Column(name = "created_on", nullable = false)
    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private String description;

    @Column(name = "event_date", nullable = false)
    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", referencedColumnName = "id", nullable = false)
    private User initiator;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", referencedColumnName = "id", nullable = false)
    private Location location;

    private boolean paid;

    @Column(name = "participant_limit", nullable = false)
    private Integer participantLimit;

    @Column(name = "published_on", nullable = false)
    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Column(nullable = false)
    private EventState state;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long views;
}
