package org.core.backend.ticketapp.entity;

import org.core.backend.ticketapp.common.enums.EventApprovalStatus;
import org.core.backend.ticketapp.common.enums.EventCategoryEnum;
import org.core.backend.ticketapp.common.enums.TimeZoneEnum;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Id;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank private String title;

    @NotBlank private String description;

    @NotBlank private boolean physicalEvent;

    @NotBlank private boolean freeEvent;

    @NotNull private int ticketsAvailable;

    @NotNull private int maxPerUser;

    @NotNull private String location;

    @NotNull private String locationNumber;

    @NotNull private String streetAddress;

    @NotBlank private EventCategoryEnum eventCategory;

    private String eventBanner = "event-banner.jpg";

    private boolean recurring = false;

    @NotNull
    private TimeZoneEnum timeZone = TimeZoneEnum.valueOf("WAT");

    @NotNull private LocalDate eventDate;

    @NotNull private LocalTime eventTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Enumerated(EnumType.STRING)
    private EventApprovalStatus approvalStatus;

    private boolean approvalRequired;

    @ElementCollection
    @CollectionTable(name = "event_seat_sections", joinColumns = @JoinColumn(name = "event_id"))
    @MapKeyColumn(name = "section_name")
    @Column(name = "seat_capacity")
    private Map<String, Integer> seatSections;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();
}
