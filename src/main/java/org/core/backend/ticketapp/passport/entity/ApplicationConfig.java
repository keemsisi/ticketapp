package org.core.backend.ticketapp.passport.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.core.backend.ticketapp.common.enums.ApplicationConfigType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "application_config")
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class ApplicationConfig extends AbstractBaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "name", columnDefinition = "varchar(250)")
    private ApplicationConfigType type;

    @Type(type = "JSONB")
    @Column(name = "data", columnDefinition = "text")
    private Object data;
}
