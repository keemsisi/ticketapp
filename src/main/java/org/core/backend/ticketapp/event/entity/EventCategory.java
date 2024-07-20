package org.core.backend.ticketapp.event.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_category")
@Getter
@Setter
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class EventCategory extends AbstractBaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
    @Column
    private String description;

    @Column
    @Type(type = "JSONB")
    private List<String> keywords;

    @PrePersist
    public void onCreate() {
        if (this.id == null) this.id = UUID.randomUUID();
        if (this.dateCreated == null) this.dateCreated = LocalDateTime.now();
    }

}
