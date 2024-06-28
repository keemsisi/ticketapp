package org.core.backend.ticketapp.passport.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TypeDef;
import org.hibernate.type.PostgresUUIDType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "read_notification")
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "UUID", typeClass = PostgresUUIDType.class)
public class ReadNotification {
    @EmbeddedId
    private ReadNotificationId readNotificationId;
    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_created", columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime dateCreated;
}
