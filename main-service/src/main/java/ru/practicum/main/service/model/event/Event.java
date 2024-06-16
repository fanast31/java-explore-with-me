package ru.practicum.main.service.model.event;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.main.service.model.Category;
import ru.practicum.main.service.model.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 20, max = 2000)
    private String annotation;

    @Column(name = "confirmed_request")
    private int confirmedRequests = 0;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Size(min = 20, max = 7000)
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    private Boolean paid = false;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration = false;

    @Size(min = 3, max = 120)
    private String title;

    private long views = 0;


    @Enumerated(value = EnumType.STRING)
    private EventState state;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

}