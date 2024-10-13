package org.core.backend.ticketapp.marketing.entity;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Entity;
import javax.persistence.Table;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "event_promotion")
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class EventPromotion extends AbstractBaseEntity {
}
