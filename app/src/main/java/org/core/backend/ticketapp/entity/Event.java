package org.core.backend.ticketapp.entity;

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

    @Column(name = "title")
    @NotBlank
    private String title;

    @Column
    private String description;

    @Column
    @NotNull
    private String location;

    @Column
    private String eventBanner = "event-banner.jpg";

    @Column
    private boolean recurring = false;

    @Column(name = "time_zone")
    @NotNull
    private TimeZoneEnum timeZone = TimeZoneEnum.valueOf("WAT");

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @Column(name = "categoryId")
    private Integer categoryId;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "ticket_id", referencedColumnName = "id")
    @Column(name = "ticket_id")
    private Integer ticketId;

    @Column
    @NotNull
    private LocalDate eventDate;

    @Column
    @NotNull
    private LocalTime eventTime;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
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
}
