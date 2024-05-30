package ru.practicum.ewmmain.request.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewmmain.event.model.Event;
import ru.practicum.ewmmain.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.practicum.ewmmain.constant.Constant.DATE_TIME_PATTERN;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requests")
@Builder(toBuilder = true)
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime created;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "id", nullable = false)
    private Event event;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", referencedColumnName = "id", nullable = false)
    private User requester;

    @Column(nullable = false)
    private String status;
}
